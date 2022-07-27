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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<%--js的导入顺序不能变化--%>
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		$("#addBtn").click(function (){

		//	获取用户信息，所有者的值
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data){// 接受一个json用户数组
					var html="<option></option>"
					$.each(data,function (i,obj){
						html+="<option value='"+obj.id+"'>"+obj.name+"</option>"
					})
					//为select标签填充html
					$("#create-owner").html(html)
					//在js中使用EL表达式时必须加双引号
					//设置默认所有者为当前登录用户
					//ajax是异步请求，为保证执行顺序，赋值操作必须放在ajax中
					var id="${user.id}"
					$("#create-owner").val(id)
				}
			})

			//获取信息完毕，显示模态窗口
			$("#createActivityModal").modal("show")
		})

		$("#saveBtn").click(function (){
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner" : $.trim($("#create-owner").val()),
					"name" : $.trim($("#create-name").val()),
					"startDate" : $.trim($("#create-startDate").val()),
					"endDate" : $.trim($("#create-endDate").val()),
					"cost" : $.trim($("#create-cost").val()),
					"description" : $.trim($("#create-description").val())
				},
				type:"post",
				dataType: "json",
				success:function (data){
					if(data.success){
					//	清空模态窗口中所有的值（jquery无法使用reset()，需要转化为js对象才能使用）
						$("#activityAddForm")[0].reset()
					//	刷新市场活动列表
                    //     pageList(1,2)
					/*	分页设置插件：pageList(操作后停留在当前页，操作后维持用户设置好的分页记录数)
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
					 */
						//做完添加操作后，回到第一页，并维持分页数
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));


						//	关闭模态窗口
						$("#createActivityModal").modal("hide")
					}else{
						alert("添加市场活动失败")
					}
				}
			})
		})

		$("#editBtn").click(function (){
			var $xz=$("input[name=xz]:checked")
			if($xz.length==0){
				alert("请选择要修改的记录")
			}else if($xz.length>1){
				alert("只能选择一条记录进行修改")
			}else {
				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						id:$xz.val()
					},
					type:"get",
					dataType:"json",
					success:function (data){
						//{"uList":[{用户1},{2},{3}],"a":{市场活动}}

						//处理所有者下拉框
						var html=""
						$.each(data.uList,function (i,obj){
							html+="<option value='"+obj.id+"'>"+obj.name+ "</option>"
						})
						$("#edit-owner").html(html)

					//	处理单条activity
						$("#edit-id").val(data.a.id)
						$("#edit-owner").val(data.a.owner)
						$("#edit-name").val(data.a.name)
						$("#edit-startDate").val(data.a.startDate)
						$("#edit-endDate").val(data.a.endDate)
						$("#edit-cost").val(data.a.cost)
						$("#edit-description").val(data.a.description)
					//	所有值都填写完毕后，打开模态窗口
						$("#editActivityModal").modal("show")
					}
				})
			}
		})

        $("#updateBtn").click(function (){
            $.ajax({
                url:"workbench/activity/update.do",
                data:{
                    "id": $.trim($("#edit-id").val()),
                    "owner" : $.trim($("#edit-owner").val()),
                    "name" : $.trim($("#edit-name").val()),
                    "startDate" : $.trim($("#edit-startDate").val()),
                    "endDate" : $.trim($("#edit-endDate").val()),
                    "cost" : $.trim($("#edit-cost").val()),
                    "description" : $.trim($("#edit-description").val())
                },
                type:"post",
                dataType: "json",
                success:function (data){
                    if(data.success){
                        //	刷新市场活动列表
                        // pageList(1,2)
						//修改后，维持当前页和分页数
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						//	关闭模态窗口
                        $("#editActivityModal").modal("hide")
                    }else{
                        alert("修改市场活动失败")
                    }
                }
            })
        })


		//页面加载完毕后，展示第一页，两条数据
		pageList(1,2)

	//	为查询按钮绑定事件，触发pageList方法
		$("#searchBtn").click(function (){

			//点击查询按钮时，将搜索框中的信息保存到隐藏域中（避免和分页冲突）
			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-startDate").val($.trim($("#search-startDate").val()))
			$("#hidden-endDate").val($.trim($("#search-endDate").val()))
			//回到第一页并维持分页数
			pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

		})

		//	为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function (){
			$("input[name=xz]").prop("checked",this.checked)
		})
		//	动态生成的元素是不能够以普通绑定事件的形式来进行操作的
		// 	$("input[name=xz]").click(function (){})
		//	只能以静态的外层元素通过on()方法来绑定事件
		$("#activityBody").on("click",$("input[name=xz]"),function (){
			//子框影响全选框
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)
		})

		$("#deleteBtn").click(function (){
		//	找到复选框中所有选中的jquery对象
			var $xz=$("input[name=xz]:checked")

			if($xz.length==0){
				alert("请选择需要删除的记录")
			}else{
				if(confirm("确定删除所选中的记录吗？")){
						//	json不允许键重复，所以通过传统方式传递参数
						//	url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx

						//	拼接参数
						var param=""
						//	将$xz中每一个dom对象遍历取值（获得每个复选框的id）
						for(var i=0;i<$xz.length;i++){
							param+= "id="+$($xz[i]).val()
							//如果不是最后一个元素，需要在后面追加一个“&”
							if(i<$xz.length-1){
								param+="&"
							}
						}
						alert(param)

						$.ajax({
							url:"workbench/activity/delete.do",
							data:param,
							type:"post",
							dataType:"json",
							success:function (data){
								if(data.success){
									//	删除成功后
									// pageList(1,2)
									//删除后，回到第一页，维持分页数
									pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

								}else {
									alert("删除市场活动失败")
								}
							}
						})
					}
				}
		})
		//为time类的输入框绑定日历控件
		$(".time").datetimepicker({
			language:  "zh-CN",
			format: "yyyy-mm-dd",//显示格式
			minView: "month",//设置只显示到月份
			autoclose: true,//选中自动关闭
			todayBtn: true, //显示今日按钮
			clearBtn : true,
			pickerPosition: "bottom-left"
		});

	});

	//pageNo：页码	pageSize：每页展示的记录数
	function pageList(pageNo,pageSize){

		//将全选的复选框的√去除，解决执行删除操作后的复选框问题
		$("#qx").prop("checked",false)

		//分页自动查询前，将隐藏域中保存的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()))
		$("#search-owner").val($.trim($("#hidden-owner").val()))
		$("#search-startDate").val($.trim($("#hidden-startDate").val()))
		$("#search-endDate").val($.trim($("#hidden-endDate").val()))

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val())
			},
			type:"get",
			dataType:"json",
			success:function (data){
				//{"total":100,"dataList":{{市场活动1},{2},{3}}}
				var html=""
				$.each(data.dataList,function (i,obj){
					html+='<tr class="active">'
					html+='<td><input type="checkbox" name="xz" value="'+obj.id+'"/></td>'
					html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+obj.id+'\';">'+obj.name+'</a></td>'
					//需要在后端进行多表联查
					html+='<td>'+obj.owner+'</td>'
					html+='<td>'+obj.startDate+'</td>'
					html+='<td>'+obj.endDate+'</td>'
					html+='</tr>'
				})
				$("#activityBody").html(html)

			//	计算总页数
				var totalPages=data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1

			//	数据填充完毕后，结合分页查询，向前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}
		})
	}
</script>
</head>
<body>

	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-startDate"/>
	<input type="hidden" id="hidden-endDate"/>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">
<%--								  <option>zhangsan</option>--%>
<%--								  <option>lisi</option>--%>
<%--								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="create-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id"/>
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
<%--								  <option>zhangsan</option>--%>
<%--								  <option>lisi</option>--%>
<%--								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endDate" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
<%--								<textarea></textarea>需要紧挨着，中间不要有内容
									textarea虽然是标签对的形式，但也属于表单元素，应该使用val()方法（而不是html()）
--%>
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon ">结束日期</div>
					  <input class="form-control time" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn" ><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>