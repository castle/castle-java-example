<jsp:useBean id="currentSessionUser" class="io.castle.example.model.TestUser" scope="session"></jsp:useBean>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var = "currentUser" value = "${currentSessionUser.getLogin()}"/>
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
        </head>
        <body>
        <h2>Welcome to Castle World</h2>
        <div>
            <h3>Logged-in user:</h3>
            <c:out value="${currentSessionUser.getUsername()}"/>
            </p>
        </div>
        <form action="logout" method="post">
        <input type="submit" value="Logout"/>
    </c:when>
    <c:otherwise>
        <html>
        <head>
            <script type="text/javascript">
                <jsp:include page="_castle_script.jsp"/>
            </script>
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
            <h2>Test data</h2>
            <p>The example application contains a built-in list of users with the logins:</p>
            <ul>
                <li>admin@example.com:admin</li>
                <li>josh@example.com:anyPassword</li>
            </ul>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>
