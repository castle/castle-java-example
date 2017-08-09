<%@ page import="java.io.IOException" %>
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
<h2>This is a page for the challenge. Please choose the correct button.</h2>
<form action="challenge_succeeded" method="post">
    <input type="submit" value="Challenge-succeed"/>
</form>
<form action="challenge_failed" method="post">
    <input type="submit" value="Challenge-fail"/>
</form>
</body>
</html>