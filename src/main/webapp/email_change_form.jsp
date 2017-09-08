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
//                secure mode off
                _castle('setUser', {
                    id: '<c:out value = "${currentUser}"/>'
                });
//                secure mod on
                _castle('secure',
                    '<%= Castle.sdk().secureUserID(currentSessionUser.getId().toString()) %>');
            </script>
            <title>Update Your Email Account</title>
        </head>
        <body>
        <h2>Update your email account</h2>
        <form action="/email_change_request" method="POST">
            <div>Select a new email address to associate with your account.</div>
            <div>
                <div class="input-prepend input-block-level">
                    <span class="add-on"><i class="icon-user"></i></span>
                    <input type="text" placeholder="Email" autocorrect="off" autocapitalize="off" autocomplete="off"
                           value="<c:out value = "${currentSessionUser.getLogin()}"/>"
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
            <form method="post">
                <button formaction="/">Cancel</button>
                <button formaction="logout">Logout</button>
            </form>
        </div>
        </body>
        </html>
    </c:when>
    <c:otherwise>
        <% response.sendError(403); %>
    </c:otherwise>
</c:choose>

