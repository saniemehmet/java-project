$(document).ready(function() {
	$.ajax({
		url:"loggedUser",
		method:"GET",
		success: function(data){
			$('#profile-fullname').val(data.name);
			$('#profile-email').val(data.email);
			$('#profile-password').val(data.password);
			$('#profile-repeat-password').val(data.password);
			$('#profile-age').val(data.age);
			if(data.userType == "doctor"){
				$('#profile-specialties').val(data.specialties);
				$('#profile-location').val(data.location);
				$('#profile-experience').val(data.experience);
				$('#profile-description').val(data.overview);
				$('#profile-contact-details').val(data.contactDetails);
			}
		},
		fail: function(){
			alert("Profile info not found!");
		}
	});
});

$('#save-profile-info').click(function(){
	let profileInfo = {
		name: $('#profile-fullname').val(),
		email: $('#profile-email').val(),
		password: $('#profile-password').val(),
		repeatPassword: $('#profile-repeat-password').val(),
		age: $('#profile-age').val(),
		specialties: $('#profile-specialties').val(),
		location: $('#profile-location').val(),
		experience: $('#profile-experience').val(),
		overview: $('#profile-description').val(),
		contactDetails: $('#profile-contact-details').val()
	}
	$.ajax({
		url: "profile/edit",
		method: "POST",
		data:{
			profileInfo: JSON.stringify(profileInfo)
		},
		success: function(data){
			if(Object.keys(data).length!=0){
				alert("Profile info saved sucessfully");
			}
			else{
				alert("Profile info is not updated!");
			}
		},
		fail: function(){
			alert("Profile info update failed!");
		}
	});
});
