<html>
<head>
    <script type="text/javascript">
        <jsp:include page="_castle_script.jsp"/>
    </script>
    <title>Password Reset Error</title>
</head>
<body>
<h2>There is no account associated to that login in our databases. Please provide a valid account.</h2>
<form method="get" id="j_reset_error">
    <button formaction="/index.jsp" type="submit" value=>Home</button>
    <button formaction="/forgot_password.jsp" type="submit" value=>Try again</button>
</form>
</body>
</html>