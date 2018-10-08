<div id="newTopicForm">
	<g:form action="addAjax" id="${user.username}" name="addTopicForm">
		<g:textArea class="form-control" id="topicContent" name="content" rows="3" cols="50"
                    placeholder="${message(code:'create.ripple.textarea.placeholder', args:[user.profile.fullName])}"/>
		<button id="addTopicBtn" class="btn btn-primary btn-lg btn-block">
			<g:message code="create.ripple.button.label" default="Create Ripple"/>
        </button>
	</g:form>
</div>
