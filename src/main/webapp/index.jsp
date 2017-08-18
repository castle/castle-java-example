<jsp:useBean id="currentSessionUser" class="io.castle.example.model.TestUser" scope="session"></jsp:useBean>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="currentUser" value="${currentSessionUser.getId()}"/>
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
            <title>Home</title>
        </head>
        <body>
        <h2>Welcome to Castle World</h2>
        <div>
            <h3>These are your account details:</h3>
            id: <c:out value="${currentUser}"/><br>
            Login: <c:out value="${currentSessionUser.getLogin()}"/> <a href="email_change_form.jsp">Change</a><br>
            Name: <c:out value="${currentSessionUser.getUsername()}"/><br>
            Lastname: <c:out value="${currentSessionUser.getLastname()}"/><br>
            </p>
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
        <html>
        <head>
            <script type="text/javascript">
                <jsp:include page="_castle_script.jsp"/>
            </script>
            <title>Home</title>
        </head>
        <body>
        <h2>Welcome to Castle World</h2>
        <form action="login" method="POST">
            <div>
                <div class="input-prepend input-block-level">
                    <span class="add-on"><i class="icon-user"></i></span>
                    <input type="text" placeholder="Login" autocorrect="off" autocapitalize="off" autocomplete="off"
                           spellcheck="false" id="j_username" name="username">
                </div>
            </div>
            <div>
                <div class="input-prepend input-block-level">
                    <span class="add-on"><i class="icon-lock"></i></span>
                    <input type="password" placeholder="Password" autocorrect="off" autocapitalize="off"
                           autocomplete="off"
                           spellcheck="false" id="j_password" name="password">
                </div>
            </div>
            <div>
                <input type="submit" class="btn" value="Login" name="submit">
            </div>
        </form>
        <div>
            <a href="forgot_password.jsp">Forgot your password?</a>
        </div>
        <div>
            <h2>Test data</h2>
            <p>The example application contains a built-in list of users with the following logins:</p>
            <ul>
                <li>admin@example.com:admin</li>
                <li>josh@example.com:anyPassword</li>
            </ul>
        </div>
        </body>
        </html>
    </c:otherwise>
</c:choose>

