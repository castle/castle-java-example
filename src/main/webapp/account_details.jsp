<jsp:useBean id="currentSessionUser" class="io.castle.example.model.TestUser" scope="session"></jsp:useBean>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="currentUser" value="${currentSessionUser.getLogin()}"/>
<c:choose>
    <c:when test="${currentUser != null }">
        <html>
        <head>
            <script type="text/javascript">
                <jsp:include page="_castle_script.jsp"/>
                _castle('setUser', {
                    id: '<c:out value = "${currentUser}"/>'
                });
            </script>
            <title>Account Details</title>
        </head>
        <body>
        <h2>These are your account details:</h2>
        <div>
            <form action="/update_account" method="post">
                id: <br>
                Login: <c:out value="${currentUser}"/> <a href="email_change_form.jsp">Change</a><br>
                Name: <input type="text" name="name" value=<c:out value="${currentSessionUser.getUsername()}"/>><br>
                Lastname: <input type="text" name="lname" value=<c:out value=""/>><br>
                <input type="submit" value="Change details">
            </form>
        </div>
        <div>
            <a href="password_change_form.jsp">Change your password</a>
        </div>
        <div>
            <form action="logout" method="post">
                <input type="submit" value="Logout"/>
            </form>
        </div>
        </body>
        </html>
    </c:when>
    <c:otherwise>
        <% response.sendError(403); %>
    </c:otherwise>
</c:choose>

