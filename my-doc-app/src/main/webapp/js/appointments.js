let appointments = [];
let userType = "";

function renderAppointmentsList(){
    $appointmentsList = $('#appointments-list');
    $appointmentsList.empty();
    if(appointments == null || appointments.length == 0){
        $('#appointments-to-show').removeAttr('class');
    }else{
        $('#appointments-to-show').attr('class','d-none');
        appointments.forEach(appointment => {
            const $template = getAppointmentTemplate(appointment);
            $appointmentsList.append($template);
        });
    }
}

function getAppointmentTemplate(appointment){
    const $template = $($('#appointments-template').html());
    $template.find('#appointment-reserved-date').text(appointment.date);
	if(appointment.date == moment(new Date()).format("DD/MM/YYYY")){
		$template.find('#appointment-reserved-date').attr('class','card-header bg-info border-success');
	}
    $template.find('#appointment-doctor-name').text(appointment.doctor.name);
    $template.find('#appointment-patient-name').text(appointment.patient.name);
    $template.find('#appointment-doctor-location').text(appointment.doctor.location);
    $template.find('#appointment-doctor-hour').text(appointment.hour);
    $template.find('#appointment-reason').text(appointment.reason);
    $template.find('#appointment-status').text(appointment.status);
    if(userType == "doctor"){
		$template.find('#appointment-doctor-name-list').attr('class','list-group-item d-none');
		$template.find('#appointment-patient-name-list').attr('class','list-group-item');
		$template.find('#appointment-location-list').attr('class','list-group-item d-none');
		$template.find('#appointment-reason-list').attr('class','list-group-item');
		$template.find('#appointment-edit-or-remove-btns').attr('class','d-flex justify-content-center d-none');
		if(appointment.status.toLowerCase() == "pending"){
			$template.find('#appointment-status-list').attr('class','list-group-item d-flex justify-content-center d-none');
			$template.find('#appointment-set-status-btns').attr('class','d-flex justify-content-center');
		}else{
			$template.find('#appointment-status-list').attr('class','list-group-item d-flex justify-content-center');
			$template.find('#appointment-set-status-btns').attr('class','d-flex justify-content-center d-none');
		}
	}else{
		$template.find('#appointment-doctor-name-list').attr('class','list-group-item');
		$template.find('#appointment-patient-name-list').attr('class','list-group-item d-none');
		$template.find('#appointment-location-list').attr('class','list-group-item');
		$template.find('#appointment-reason-list').attr('class','list-group-item d-none');
		$template.find('#appointment-status-list').attr('class','list-group-item d-flex justify-content-center');
		$template.find('#appointment-set-status-btns').attr('class','d-flex justify-content-center d-none');
		if(appointment.status.toLowerCase() == "pending"){
			$template.find('#appointment-edit-or-remove-btns').attr('class','d-flex justify-content-center');
		}else{
			$template.find('#appointment-edit-or-remove-btns').attr('class','d-flex justify-content-center d-none');
		}
	}
	$template.find('#edit-appointment').click(function(){
		$.ajax({
				url:"schedule/doctor/search",
				method:"GET",
				data:{
					doctorId: appointment.doctor.id,
					date: appointment.date
				},
				success: function(data){
					$.ajax({
						url:"appointment/find/reserved",
						method:"GET",
						data:{
							doctorId: appointment.doctor.id,
							date: appointment.date
						},
						success: function(reservedHours){
							if(reservedHours.length != 0){
								$('#appointment-hour-edit').empty();
								for(let i=data.fromHour;i<data.toHour;i++){
									if(!reservedHours.includes(i)){
										$('#appointment-hour-edit').append($('<option>', {
									    value: i,
									    text: (i<10?"0"+i+":00":i+":00")
										}));	
									}
								}
							}else{
								$('#appointment-hour-edit').empty();
								for(let i=data.fromHour;i<data.toHour;i++){
									$('#appointment-hour-edit').append($('<option>', {
									    value: i,
									    text: (i<10?"0"+i+":00":i+":00")
									}));
								}
							}
						},fail:function(){
							
						}
					});
				},
				fail:function(){
					alert("Schedule not available");
				}
		});
		$('#save-edited-appointment-hour').click(function(){
			$.ajax({
				url:"/appointment/edit",
				method: "POST",
				data:{
					doctorId: appointment.doctor.id,
					date: appointment.date,
					hour: $('#appointment-hour-edit').val()
				},
				success: function(data){
					console.log(data);
					getAllAppointments();
				},
				fail: function(){
					alert("Appointment approval failed");
				}
			});
			$('#appointment-hour-edit').empty();
			$('#editAppointmentModal').modal('hide');
		});
	});
	$template.find('#remove-appointment').click(function(){
		$.ajax({
			url:"appointment/delete",
			method:"DELETE",
			data:{
				doctorId: appointment.doctor.id,
				date: appointment.date
			},
			success:function(data){
				if(data==true){
					getAllAppointments();
				}else{
					alert("Delete operation failed");
				}
			},
			fail:function(){
				alert("Delete request failed");
			}
		});
	});
    $template.find('#approve-appointment').click(function(){
	    $.ajax({
			url:"/appointment/approve",
			method: "POST",
			data:{
				patientId: appointment.patient.id,
				date: appointment.date
			},
			success: function(data){
				console.log(Object.keys(data).length);
				if(data!=null && Object.keys(data).length!=0){
					alert("Appointment approved successfully");
					getAllAppointments();
				}else{
					alert("Appointment not found");
				}
			},
			fail: function(){
				alert("Appointment approval failed");
			}
		});
	});
    $template.find('#reject-appointment').click(function(){
		$.ajax({
			url:"/appointment/reject",
			method: "POST",
			data:{
				patientId: appointment.patient.id,
				date: appointment.date
			},
			success: function(data){
				console.log(Object.keys(data).length);
				if(data!=null && Object.keys(data).length!=0){
					alert("Appointment rejected");
					getAllAppointments();
				}else{
					alert("Appointment not found");
				}
			},
			fail: function(){
				alert("Appointment approval failed");
			}
		});
	});
    return $template;
}

function getAllAppointments(){
	$.ajax({
		url:"/appointments/all",
		method:"GET",
		success: function(data){
			appointments = data;
			renderAppointmentsList();
		},
		fail: function(){
			alert("Get appointments request failed");
		}
	});
}

$.ajax({
	url: "loggedUserType",
	method: "GET",
	success: function(type){
		userType = type;
	},
	fail: function(){
		alert("User type not resolved");
	}
});

getAllAppointments();