$(document).ready(function() {
	$("#addTopicBtn").click(function() {
		showSpinner(true);
		$("#topicContent").fadeTo("fast", 0.4);
		$(this).attr('disabled', 'disabled');
		jQuery.ajax({
			type:'POST',
			data:jQuery(this).parents('form:first').serialize(), 
			url:'/ripple/addAjax',
			success: function(data,textStatus){
				jQuery('#allTopics').html(data);
				clearTopic(data);
			},
			error: function(XMLHttpRequest,textStatus,errorThrown){
			},
			complete: function(XMLHttpRequest,textStatus){
				showSpinner(false);
				$("#topicContent").fadeTo("fast", 1.0);
				$('#addTopicBtn').removeAttr('disabled');
				$('#addTopicBtn').blur();
			}
		});
		return false;
	});
})
function clearTopic(e) {
	$('#topicContent').val('');
}
function showSpinner(visible) {
	if (visible) {
		console.log("Showing stuff...");
		//$("#newPost textArea, #newPost .btn").attr('disabled', 'disabled');
		$('.spinner').slideDown();
	} else {
		console.log("Hiding stuff...");
		//$("#newPost textArea, #newPost .btn").attr('disabled', '');
		$('.spinner').slideUp();
	}
}
