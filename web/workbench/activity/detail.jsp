<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#E6E6E6");
		});

	//	页面加载完毕后，展现该市场活动关联的备注信息列表
		showRemarkList()
		//修改和删除的图标
		$("#remarkBody").on("mouseover",".remarkDiv",function(){
			$(this).children("div").children("div").show();
		})
		$("#remarkBody").on("mouseout",".remarkDiv",function(){
			$(this).children("div").children("div").hide();
		})

	//	为备注保存按钮添加事件
		$("#saveRemarkBtn").click(function (){
			$.ajax({
				url:"workbench/activity/saveRemark.do",
				data:{
					"activityId" : "${activity.id}",
					"noteContent" : $.trim($("#remark").val())
				},
				type:"post",
				dataType:"json",
				success:function (data){
				//	{"success":true/false,"ar":{备注1}}
					if(data.success){
					//	添加成功后在清空文本域并在其上方新增一个div
						$("#remark").val("")
						var html=""
						var obj=data.ar
						html+='	<div id="'+obj.id+'" class="remarkDiv" style="height: 60px;">'
						html+='	<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
						html+='	<div style="position: relative; top: -40px; left: 40px;" >'
						html+='	<h5 id="edit'+obj.id+'">'+obj.noteContent+'</h5>'
						html+='	<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray; id="small'+obj.id+'">'+obj.createTime+' 由'+obj.createBy+'</small>'
						html+='	<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
						html+='	<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+obj.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: red;"></span></a>'
						html+='	&nbsp;&nbsp;&nbsp;&nbsp;'
						//动态生成的元素的函数参数必须用引号包裹
						html+='	<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+obj.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: red;"></span></a>'
						html+='	</div>'
						html+='	</div>'
						html+='	</div>'

						$("#remark").before(html)
					}else{
						alert("添加备注失败")
					}
				}

			})
		})

	//	修改模态窗口的保存事件
		$("#updateRemarkBtn").click(function (){
			//获取备注的id
			var id=$("#remarkId").val()
			$.ajax({
				url:"workbench/activity/updateRemark.do",
				data:{
					"id" : id,
					"noteContent": $.trim($("#noteContent").val())
				},
				type:"post",
				dataType:"json",
				success:function (data){
				//	{"success":true/false,"ar":{备注1}}
					if(data.success){
					//	更新原div中的内容，需要更新noteContent，editTime,editBy
						$("#edit"+id).html(data.ar.noteContent)
						$("#small"+id).html(data.ar.editTime+' 由'+data.ar.editBy)

					//	更新完后关闭模态窗口
						$("#editRemarkModal").modal("hide")
					}else {
						alert("修改备注失败")
					}
				}
			})
		})

	});

	function showRemarkList(){
		$.ajax({
			url:"workbench/activity/getRemarkListByAid.do",
			data:{
				activityId : "${activity.id}"
			},
			type:"get",
			dataType:"json",
			success:function (data){
				//[{备注1},{2},{3}]
				var html=""
				$.each(data,function (i,obj){
						html+='	<div id="'+obj.id+'" class="remarkDiv" style="height: 60px;">'
						html+='	<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">'
						html+='	<div style="position: relative; top: -40px; left: 40px;" >'
						html+='	<h5 id="edit'+obj.id+'">'+obj.noteContent+'</h5>'
						html+='	<font color="gray">市场活动</font> <font color="gray">-</font> <b>${activity.name}</b> <small style="color: gray;" id="small'+obj.id+'">'+(obj.editFlag==0?obj.createTime:obj.editTime)+' 由'+(obj.editFlag==0?obj.createBy:obj.editBy)+'</small>'
						html+='	<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">'
						html+='	<a class="myHref" href="javascript:void(0);" onclick="editRemark(\''+obj.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: red;"></span></a>'
						html+='	&nbsp;&nbsp;&nbsp;&nbsp;'
						//动态生成的元素的函数参数必须用引号包裹
						html+='	<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+obj.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: red;"></span></a>'
						html+='	</div>'
						html+='	</div>'
						html+='	</div>'
					})
				$("#remarkDiv").before(html)
				}
		})
	}

	function deleteRemark(id){
		if(!confirm("确定要删除该备注吗？")){
			return false
		}
		$.ajax({
			url:"workbench/activity/deleteRemark.do",
			data:{
				"id":id
			},
			type:"post",
			dataType:"json",
			success:function (data){
				//{"success":ture/false}
				if(data.success){
					//删除成功后删除内存中的记录
					$("#"+id).remove()
				}else {
					alert("删除备注失败")
				}
			}
		})
	}

	//修改的模态窗口
	function editRemark(id){

		//获取ajax中的对应文本域内容，并赋值给模态窗口的文本域
		var noteContent=$("#edit"+id).html()
		$("#noteContent").val(noteContent)

		//将id存到隐藏域，方便其它函数调用
		$("#remarkId").val(id)

		//弹出模态窗口
		$("#editRemarkModal").modal("show")

	}

	
</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
		<%-- 备注的id --%>
		<input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>



	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${activity.name} <small>${activity.startDate} ~ ${activity.endDate}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.owner}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.name}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.startDate}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${activity.endDate}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${activity.cost}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.createBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.createTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${activity.editBy}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${activity.editTime}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${activity.description}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>
		
		<!-- 备注1 -->
<%--		<div class="remarkDiv" style="height: 60px;">--%>
<%--			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">--%>
<%--			<div style="position: relative; top: -40px; left: 40px;" >--%>
<%--				<h5>哎呦！</h5>--%>
<%--				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>--%>
<%--				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">--%>
<%--					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
<%--				</div>--%>
<%--			</div>--%>
<%--		</div>--%>
<%--		--%>
<%--		<!-- 备注2 -->--%>
<%--		<div class="remarkDiv" style="height: 60px;">--%>
<%--			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">--%>
<%--			<div style="position: relative; top: -40px; left: 40px;" >--%>
<%--				<h5>呵呵！</h5>--%>
<%--				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>--%>
<%--				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">--%>
<%--					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
<%--					&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>--%>
<%--				</div>--%>
<%--			</div>--%>
<%--		</div>--%>
		
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button type="button" class="btn btn-primary" id="saveRemarkBtn">保存</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>