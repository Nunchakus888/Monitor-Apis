angular.module("DirectiveModule",[]).
directive('compare', function () {
    var o = {};
    o.strict = "AE";
    o.scope = {
        //之前的字符（开始设置的密码）
        orgText: "=compare"
    };
    o.require = 'ngModel';
    o.link = function (sco, ele, attr, con) {
        con.$validators.compare = function (v) {//v是用户输入的值（密码）
            return v == sco.orgText;
        }
        //监控orgText是否有变化
        sco.$watch('orgText', function () {
            con.$validate();
        })
    }
    return o;
})