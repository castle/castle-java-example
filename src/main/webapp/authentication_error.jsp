<html>
<head>
    <script type="text/javascript">
        <jsp:include page="_castle_script.jsp"/>
        _castle('setUser', {
            id: '1234'
        });
    </script>
</head>
<body>
<h2>Error! Please try to log-in again.</h2>
<form action="/index.jsp" method="post">
    <input type="submit" value="Home"/>
</form>
</body>
</html>