<%@ page import="java.io.IOException" %>
<jsp:useBean id="challengedUser" class="io.castle.example.model.TestUser" scope="session"></jsp:useBean>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="currentUser" value="${challengedUser.getId()}"/>
<html>
<head>
    <script type="text/javascript">
        <jsp:include page="_castle_script.jsp"/>
        _castle('setUser', {
            id: '<c:out value = "${currentUser}"/>'
        });
    </script>
    <title>Authentication Challenge</title>
</head>
<body>
<h2>This is a page for the challenge. Please choose the correct button.</h2>
<form action="challenge" method="post">
    <input type="submit" value="Challenge-succeed"/>
    <input type="hidden" name="is_challenge_succeeded" value="true">
</form>
<form action="challenge" method="post">
    <input type="submit" value="Challenge-fail"/>
    <input type="hidden" name="is_challenge_succeeded" value="false">
</form>
</body>
</html>