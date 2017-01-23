package com.sumscope.cdh.web.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ZipUtils {
	public static byte[] gzipToByteArray(String str) throws IOException {
		byte[] result=new byte[0];
		if (str == null || str.length() == 0) {
	      return result;
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    GZIPOutputStream gzip = new GZIPOutputStream(out);
	    gzip.write(str.getBytes("UTF-8"));
	    gzip.close();
	    result=out.toByteArray();
	    return result;
	}

	public static String ungzip(byte[] stream) throws IOException {
	    if (stream == null || stream.length == 0) {
	      return "";
	    }
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ByteArrayInputStream in = new ByteArrayInputStream(stream);
	    GZIPInputStream gunzip = new GZIPInputStream(in);
	    byte[] buffer = new byte[256];
	    int n;
	    while ((n = gunzip.read(buffer)) >= 0) {
	      out.write(buffer, 0, n);
	    }
	    // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
	    return out.toString("UTF-8");
	}
	
}
