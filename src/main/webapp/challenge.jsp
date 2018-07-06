<%@ page import="java.io.IOException" %>
<%@ page import="io.castle.client.Castle" %>
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
        _castle('secure',
            '<%= Castle.instance().secureUserID(challengedUser.getId().toString()) %>');
    </script>
    <title>Authentication Challenge</title>
</head>
<body>
<h2>This is a page for the challenge. Please choose the correct button.</h2>

<form action="/challenge" method="post" id="j_challenge">
    <button name="is_challenge_succeeded" type="submit" value=true>Challenge-succeed</button>
    <button name="is_challenge_succeeded" type="submit" value=false>Challenge-fail</button>
</form>

</body>
</html>