<jsp:useBean id="currentSessionUser" class="io.castle.example.model.TestUser" scope="session"></jsp:useBean>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="currentUser" value="${passwordResetUser.getLogin()}"/>
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
            <title>Password Reset</title>
        </head>
        <body>
        <h2>Reset your password</h2>
        <form action="/password_reset" method="POST">
            <div>
                <div class="input-prepend input-block-level">
                    <span class="add-on"><i class="icon-user"></i></span>
                    <input type="password" placeholder="New password" autocorrect="off" autocapitalize="off"
                           autocomplete="off"
                           spellcheck="false" id="j_password" name="new_password">
                </div>
            </div>
            <div>
                <div class="input-prepend input-block-level">
                    <span class="add-on"><i class="icon-lock"></i></span>
                    <input type="password" placeholder="Confirm new password" autocorrect="off" autocapitalize="off"
                           autocomplete="off"
                           spellcheck="false" id="j_password_confirm" name="new_password_confirm">
                </div>
            </div>
            <div>
                <input type="submit" class="btn" value="Submit" name="submit">
                <input type="hidden" name="password_reset" value="true"/>
            </div>
        </form>
        </body>
        </html>
    </c:when>
    <c:otherwise>
        <% response.sendError(403); %>
    </c:otherwise>
</c:choose>

