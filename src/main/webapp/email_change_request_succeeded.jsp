<%@ page import="io.castle.client.Castle" %>
<jsp:useBean id="currentSessionUser" class="io.castle.example.model.TestUser" scope="session"></jsp:useBean>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="currentUser" value="${currentSessionUser.getId()}"/>
<c:choose>
    <c:when test="${currentUser != null }">
        <html>
        <head>
        <script type="text/javascript">
        <jsp:include page="_castle_script.jsp"/>
//        secure mode off
        _castle('setUser', {
        id: '<c:out value = "${currentUser}"/>'
        });
//        seure mode on
        _castle('secure',
            '<%= Castle.sdk().secureUserID(currentSessionUser.getId().toString()) %>');
        </script>
    <title>Email Update</title>
</head>
<body>

<h2>We have sent you a message to the email address you specified. Did you receive it?</h2>
<form action="/email_change" method="post" id="j_email_change_success">
    <button name="should_email_change" type="submit" value=true>Yes, and I confirm that I have control over that email account.</button><br>
    <button name="should_email_change" type="submit" value=false>Something is wrong. Don't update my email.</button>
</form>
</body>
</html>
    </c:when>
    <c:otherwise>
        <% response.sendError(403); %>
    </c:otherwise>
</c:choose>
