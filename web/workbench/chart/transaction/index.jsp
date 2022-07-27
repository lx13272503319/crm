<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
  <base href="<%=basePath%>">
  <script src="ECharts/echarts.min.js"></script>
  <script src="jquery/jquery-1.11.1-min.js"></script>
  <script type="text/javascript">
    $(function (){
    //  页面加载完毕后，绘制统计图表
      getCharts()
    })

    function getCharts() {
      $.ajax({
        url: "workbench/transaction/getCharts.do",
        type: "get",
        dataType: "json",
        success: function (data) {
          //{"total":100,"dataList":[{value: "数量", name: '阶段'},{2},{3}]}
          // 基于准备好的dom，初始化echarts实例
          var myChart = echarts.init(document.getElementById('main'));

          // 指定图表的配置项和数据
          var option = {
            title: {
              text: '交易漏斗图',
              subtext: '统计交易阶段数量的漏斗图'
            },
            series: [
              {
                name: '交易漏斗图',
                type: 'funnel',
                left: '10%',
                top: 60,
                bottom: 60,
                width: '80%',
                min: 0,
                max: data.total,
                minSize: '0%',
                maxSize: '100%',
                sort: 'descending',
                gap: 2,
                label: {
                  show: true,
                  position: 'inside'
                },
                labelLine: {
                  length: 10,
                  lineStyle: {
                    width: 1,
                    type: 'solid'
                  }
                },
                itemStyle: {
                  borderColor: '#fff',
                  borderWidth: 1
                },
                emphasis: {
                  label: {
                    fontSize: 20
                  }
                },
                data: data.dataList
                        // [
                  // {value: "数量", name: '阶段'},
                  // {value: 40, name: 'Inquiry'},
                  // {value: 20, name: 'Order'},
                  // {value: 80, name: 'Click'},
                  // {value: 100, name: 'Show'}
                // ]
              }
            ]
          };

          // 使用刚指定的配置项和数据显示图表。
          myChart.setOption(option);
        }
      })
    }


  </script>
    <title>Title</title>
</head>
<body>
<%--为Echarts准备一个具有宽高的dom容器--%>
  <div id="main" style="width: 600px;height: 400px;"></div>
</body>
</html>
