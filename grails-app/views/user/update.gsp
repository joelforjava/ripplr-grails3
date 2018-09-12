<html>
	<head>
		<title>Update Profile</title>
		<meta name="layout" content="main">
	</head>
	<body>
        <div class="border-bottom mb-4">
            <h1>Update Your Profile</h1>
        </div>
		<div class="row">
			<g:hasErrors>
				<div class="errors alert alert-danger">
					<g:renderErrors bean="${user}" as="list" />
				</div>
			</g:hasErrors>
			<g:if test="${flash.message}">
				<div class="flash">${flash.message}</div>
			</g:if>
			<div class="col-md-2">
			</div>
			<div class="col-md-6">
				<g:uploadForm useToken="true" action="updateProfile">
					<g:render template="userForm" model="[user: user]" />
					<fieldset class="buttons">
						<g:submitButton class="btn btn-primary" name="update" value="Update!" />
					</fieldset>
				</g:uploadForm>
			</div>
		</div>
	</body>
</html>