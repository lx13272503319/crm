<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <title>mytitle</title>
</head>
<body>

        $.ajax({

            url : "",
            data : {

            },
            type : "",
            dataType : "json",
            success : function (data) {



            }

        })

        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        //为time类的输入框绑定日历控件
        $(".time").datetimepicker({
        language:  "zh-CN",
        format: "yyyy-mm-dd",//显示格式
        minView: "month",//设置只显示到月份
        autoclose: true,//选中自动关闭
        todayBtn: true, //显示今日按钮
        clearBtn : true,
        pickerPosition: "top-left"
        });

        String createTime= DateTimeUtil.getSysTime();
        String createBy=((User)request.getSession().getAttribute("user")).getName();
</body>
</html>
