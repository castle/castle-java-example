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
            <title>Change Your Email</title>
        </head>
        <body>
        <h2>Change your Email</h2>
        <form action="/email_change" method="POST">
            <div>
                <div class="input-prepend input-block-level">
                    <span class="add-on"><i class="icon-user"></i></span>
                    <input type="text" placeholder="Email" autocorrect="off" autocapitalize="off" autocomplete="off"
                           value="<c:out value = "${currentUser}"/>"
                           spellcheck="false" id="j_username" name="username">
                </div>
            </div>
            <div>Please type your password in order to confirm.</div>
            <div>
                <div class="input-prepend input-block-level">
                    <span class="add-on"><i class="icon-user"></i></span>
                    <input type="password" placeholder="Current password" autocorrect="off" autocapitalize="off"
                           autocomplete="off"
                           spellcheck="false" id="j_password" name="password">
                </div>
            </div>
            <div>
                <input type="submit" class="btn" value="Submit" name="submit">
            </div>
        </form>
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

