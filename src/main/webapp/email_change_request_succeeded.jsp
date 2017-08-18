<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript">
        <jsp:include page="_castle_script.jsp"/>
    </script>
    <title>Email Update</title>
</head>
<body>
<h2>We have sent you a message to the email address you specified. Did you receive it?</h2>
<form action="/email_change" method="post" id="j_email_change_success" name="email_change_next_step">
    <input type="submit" value="Yes, and I confirm that I have control over that email account."/>
    <input type="hidden" name="should_email_change" value="true"/>
</form>
<form action="/email_change" method="post" id="j_password_reset_fail" name="password_reset_fail">
    <input type="submit" value="Something is wrong. Don't update my email."/>
    <input type="hidden" name="should_email_change" value="false"/>
</form>
</body>
</html>
