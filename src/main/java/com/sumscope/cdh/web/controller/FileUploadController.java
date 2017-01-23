package com.sumscope.cdh.web.controller;

import com.sumscope.cdh.sumscopezk4j.zookeeper.ResourceUtil;
import com.sumscope.cdh.web.domain.EchoCode;
import com.sumscope.cdh.web.domain.JsonObj;
import com.sumscope.cdh.web.domain.ResultObj;
import com.sumscope.cdh.web.service.ZookeeperService;
import com.sumscope.cdh.web.util.DateUtil;
import com.sumscope.cdh.web.util.JsonUtil;
import com.sumscope.cdh.sumscopezk4j.zookeeper.ZookeeperNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by wenshuai.li on 2016/10/31.
 */
@Controller
@RequestMapping(value = "/configManage", produces = "application/json; charset=UTF-8")
public class FileUploadController {

    @Autowired
    private ZookeeperService zookeeperService;

    @Value("${spring.zookeeper.root.path}")
    private String ROOT_PATH;


    @RequestMapping(value = "/addAndUpload", method = RequestMethod.POST)
    @ResponseBody
    public  String addAndUpload( @RequestParam("file") MultipartFile file,
                                      @RequestParam("path") String path,
                                      @RequestParam("name") String name) {
        ResultObj resultObj = new ResultObj();
        JsonObj jsonObj = new JsonObj();
        resultObj.setStatus(false);

        if (!file.isEmpty()) {
            try {
                String config = parse(file);

                if(!zookeeperService.exists(path + "/" + name)){
                    jsonObj = zookeeperService.createNode(path,name,config, ZookeeperNode.NodeType.TEXT.value());
                    resultObj.setStatus(true);
                }else{
                    throw new IllegalArgumentException(path + "/" + name + " 已经存在");
                }

            } catch (IllegalArgumentException e){
                resultObj.setMsg(e.getMessage());
                resultObj.setEchoCode(EchoCode.CREATE_ERROR);
                jsonObj.setResult(resultObj);
            }
            catch (Exception e) {
                resultObj.setMsg("添加文件失败");
                resultObj.setEchoCode(EchoCode.UPLOAD_ERROR);
                jsonObj.setResult(resultObj);
            }
        } else {
            resultObj.setMsg("添加文件失败，文件为空。");
            resultObj.setEchoCode(EchoCode.UPLOAD_ERROR);
            jsonObj.setResult(resultObj);
        }

        return JsonUtil.writeValueAsString(jsonObj);
    }

    @RequestMapping(value = "/updateAndUpload", method = RequestMethod.POST)
    @ResponseBody
    public  String updateAndUpload( @RequestParam("file") MultipartFile file,
                                 @RequestParam("path") String path,
                                 @RequestParam("name") String name) {
        ResultObj resultObj = new ResultObj();
        JsonObj jsonObj = new JsonObj();
        resultObj.setStatus(false);
        if (!file.isEmpty()) {
            try {
                String config = parse(file);

                if(!zookeeperService.exists(path)){
                    throw new IllegalArgumentException(path  + " node not exists");
                }else{
                    jsonObj = zookeeperService.updateData(path,name,config);
                    resultObj.setStatus(true);
                }

            } catch (IllegalArgumentException e){
                resultObj.setMsg(e.getMessage());
                resultObj.setEchoCode(EchoCode.UPDATE_ERROR);
                jsonObj.setResult(resultObj);
            }
            catch (Exception e) {
                resultObj.setMsg("添加文件失败");
                resultObj.setEchoCode(EchoCode.UPLOAD_ERROR);
                jsonObj.setResult(resultObj);
            }
        } else {
            resultObj.setMsg("添加文件失败，文件为空。");
            resultObj.setEchoCode(EchoCode.UPLOAD_ERROR);
            jsonObj.setResult(resultObj);
        }

        return JsonUtil.writeValueAsString(jsonObj);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    @ResponseBody
    public  String downloadFileHandler(@RequestParam("path") String path, HttpServletResponse response) throws Exception {

        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + path.replaceFirst("/","").replaceAll("/","_") + ".properties");

        OutputStream os = null;
        InputStream inputStream = null;

        JsonObj jsonObj;
        if(!zookeeperService.exists(path)){
            throw new IllegalArgumentException(path  + " node not exists");
        }else{
            try{
                jsonObj = zookeeperService.getZookeeperNodes(path,1);
                /*File file = new File(path.replaceAll("/","-"));
                OutputStream output = new FileOutputStream(file);*/

                ZookeeperNode zookeeperNode = (ZookeeperNode)jsonObj.getData();
                inputStream = new ByteArrayInputStream(zookeeperNode.getValue().getBytes(Charset.forName("utf-8")));

                os = response.getOutputStream();
                byte[] b = new byte[2048];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    os.write(b, 0, length);
                }
            }catch (Exception e){

            }finally {
                // 这里主要关闭。
                try {
                    os.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        /*if ((!fileName.isEmpty()) && fileName.contains(ZookeeperNode.COMPARE_PATH)) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
            try {

                inputStream = new FileInputStream(new File(fileName));

                os = response.getOutputStream();
                byte[] b = new byte[2048];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    os.write(b, 0, length);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {

                // 这里主要关闭。
                try {
                    os.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }*/
    }

    private String upload(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            // 文件存放服务端的位置
            String rootPath = ROOT_PATH;
            File dir = new File(rootPath + File.separator + "tmpFiles" + File.separator
                    + DateUtil.formatDateToString(new Date(),"yyyy-MM-dd"));
            if (!dir.exists())
                dir.mkdirs();
            // 写文件到服务器
            String fullName = dir.getAbsolutePath() + File.separator
                    + "[" +  DateUtil.formatDateToString(new Date(),"yyyy-MM-dd HH-mm-ss-SS")
                    +"]" + file.getOriginalFilename();
            File serverFile = new File(fullName);
            file.transferTo(serverFile);

            return fullName;
        }
        return null;
    }

    private String parse(MultipartFile file) throws IOException {
        byte[] b = file.getBytes();
        String config = new String(b);
        return config;

    }

    protected Map<String, String> propertiesConfig(byte[] data) throws IOException {
        InputStream in = new ByteArrayInputStream(data);
        Properties properties = ResourceUtil.getPro(in);
        Map<String, String> map = new HashMap((Map) properties);
        return map;
    }
}