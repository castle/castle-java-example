<html>
<head>
    <script type="text/javascript">
        <jsp:include page="_castle_script.jsp"/>
    </script>
    <title>Password Reset Error</title>
</head>
<body>
<h2>There is no account associated to that login in our databases. Please provide a valid account.</h2>
<form action="/index.jsp" method="get">
    <input type="submit" value="Home"/>
</form>
<form action="/forgot_password.jsp" method="get">
    <input type="submit" value="Try again"/>
</form>
</body>
</html>