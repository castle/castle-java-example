<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Reset Password Challenge</title>
</head>
<body>
<h2>We have sent you an email with details for resetting your password. Did you receive it?</h2>
<form action="/password_reset_form.jsp" method="get" id="j_password_reset_success" name="password_reset_next_step">
    <input type="submit" value="Yes, I have control over that email account."/>
</form>
<form action="/password_reset" method="post" id="j_password_reset_fail" name="password_reset_fail">
    <input type="submit" value="No, I cannot prove I own that email account."/>
    <input type="hidden" name="password_reset" value="false"/>
</form>
</body>
</html>
