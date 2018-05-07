<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
</head>
<body>
<div class="container">
    <h3>商品名称：${kaola.productName}</h3>
    <ul class="list-group">
        <li class="list-group-item">
            价格：${kaola.price}
        </li>
        <li class="list-group-item">
            市场价格：${kaola.marketPrice}
        </li>
        <li class="list-group-item">
            产地：${kaola.place}
        </li>
    </ul>
</div>
</body>
</html>
