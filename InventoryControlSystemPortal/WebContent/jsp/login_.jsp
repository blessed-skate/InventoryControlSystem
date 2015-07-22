<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Login Form</title>
<link rel="stylesheet" href="css/style.css">
<link rel="icon" href="imgs/icon.png" type="image/png" />
</head>
<body>
	<div class="container">
		<div class="login">
			<h1>Sistema Control de Inventarios</h1>
			<form method="post" action="j_acegi_security_check">
				<p>
					<input type="text" name="j_username" value=""
						placeholder="Username or Email">
				</p>
				<p>
					<input type="password" name="j_password" value=""
						placeholder="Password">
				</p>
				<p class="submit">
					<input type="submit" name="commit" value="Login">
				</p>
			</form>
		</div>
	</div>
</body>
</html>
