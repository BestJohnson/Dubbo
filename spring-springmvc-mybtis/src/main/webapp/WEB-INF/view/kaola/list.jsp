<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/4/10
  Time: 19:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
</head>
<body>
    <div class="container">
        <h3>商品列表</h3>
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <div class="well-sm">
            <form class="form-inline">
                <input type="text" name="kaolaName" class="form-control" placeholder="商品名称" value="${param.kaolaName}">
                <input type="text" name="place" class="form-control" placeholder="产地" value="${param.place}">
                <input type="text" name="minPrice" class="form-control" placeholder="最低价格" value="${param.minPrice}">
                <input type="text" name="maxPrice" class="form-control" placeholder="最高价格" value="${param.maxPrice}">
                <select name="typeId" class="form-control">
                    <option value="">所属分类</option>
                    <c:forEach items="${typeList}" var="type">
                        <option value="${type.id}" ${param.typeId == type.id ? 'selected' : ''}>${type.typeName}</option>
                    </c:forEach>
                </select>

                <button class="btn btn-info">搜索</button>
            </form>
        </div>
        <a href="/kaola/new" class="btn btn-primary pull-right">新增商品</a>
        <table class="table">
            <thead>
                <tr>
                    <td>商品名称</td>
                    <td>价格</td>
                    <td>市场价格</td>
                    <td>产地</td>
                    <td>所属分类</td>
                    <td>#</td>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${pageInfo.list}" var="kaola">
                    <tr>
                        <td><a href="/kaola/${kaola.id}">${kaola.productName}</a></td>
                        <td>${kaola.price}</td>
                        <td>${kaola.marketPrice}</td>
                        <td>${kaola.place}</td>
                        <td>${kaola.kaolaType.typeName}</td>
                        <td>
                            <a href="/kaola/${kaola.id}/edit">修改</a>
                            <a href="javascript:;" class="delKaola" rel="${kaola.id}">删除</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        共 ${pageInfo.total} 条记录
        <ul id="pagination-demo" class="pagination pull-right"></ul>
    </div>
    <script src="/static/js/jquery.min.js"></script>
    <script src="/static/js/bootstrap.min.js"></script>
    <script src="/static/js/jquery.twbsPagination.js"></script>


    <script>
        $(function () {

            $("#pagination-demo").twbsPagination({
               totalPages:${pageInfo.pages},
                visiblePages:5,
                first:"首页",
                last:"末页",
                prev:"上一页",
                next:"下一页",
                href:"?kaolaName="+ encodeURIComponent('${param.kaolaName}') + "&place=" + encodeURIComponent('${param.place}')
                    + "&minPrice=${param.minPrice}&maxPrice=${param.maxPrice}&typeId=${param.typeId}&p={{number}}"
            });


            $(".delKaola").click(function () {
                var id = $(this).attr("rel");
                if(confirm("确定要删除吗")) {
                    window.location.href = "/kaola/"+id+"/del";
                }
            });
        })
    </script>
</body>
</html>
