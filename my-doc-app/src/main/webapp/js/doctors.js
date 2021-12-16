let doctors = [];
let searchDoctorsList = [];

function renderDoctorsList(){
    $doctorsList = $('#doctors-list');
    $doctorsList.empty();
    if(doctors == null || doctors.length == 0){
        $('#doctors-results-to-show').removeAttr('class');
    }else{
        $('#doctors-results-to-show').attr('class','d-none');
        doctors.forEach(doctor => {
            const $template = getDoctorTemplate(doctor);
            $doctorsList.append($template);
        });
    }
}

function renderSearchDoctorsList(){
    $doctorsSearchList = $('#doctors-search-list');
    $doctorsSearchList.empty();
    if(searchDoctorsList == null || searchDoctorsList.length == 0){
        $('#search-doctors-results-to-show').removeAttr('class');
    }else{
        $('#search-doctors-results-to-show').attr('class','d-none');
        searchDoctorsList.forEach(doctor => {
            const $template = getDoctorTemplate(doctor);
            $doctorsSearchList.append($template);
        });
    }
}

function getDoctorTemplate(doctor){
    const $template = $($('#doctor-template').html());
    $template.find('#template-doctor-name').text(doctor.name);
    //$template.find('#template-doctor-age').text(doctor.age + y/o,);
    $template.find('#template-doctor-experience').text(doctor.experience+" years experience");
    $template.find('#template-doctor-specialties').text(doctor.specialties);
    $template.find('#template-doctor-location').text(doctor.location);
	if(doctor.overview.length>50){
		$template.find('#template-doctor-overview').attr('style','overflow: scroll;height: 10rem;');
	}
    $template.find('#template-doctor-overview').text(doctor.overview);
    $template.find('#template-doctor-contacts').text(doctor.contactDetails);
    $template.find('#book-appointment').click(function(){
		$('#show-schedule').click(function(){
			let doctorId = doctor.id;
			let date = $('#appointment-date').val();
			$.ajax({
				url:"schedule/doctor/search",
				method:"GET",
				data:{
					doctorId: doctorId,
					date: date
				},
				success: function(data){
					if(data.length != 0){
						$.ajax({
							url:"appointment/find/reserved",
							method:"GET",
							data:{
								doctorId:doctorId,
								date:date
							},
							success: function(reservedHours){
								if(reservedHours.length != 0){
									$('#appoitnment-select-div').attr('class','mt-3 row g-3 align-items-center');
									$('#appointment-hour-select').empty();
									for(let i=data.fromHour;i<data.toHour;i++){
										if(!reservedHours.includes(i)){
											$('#appointment-hour-select').append($('<option>', {
										    value: i,
										    text: (i<10?"0"+i+":00":i+":00")
											}));	
										}
									}
									$('#request-appointment').removeAttr("disabled");
								}else{
									$('#appoitnment-select-div').attr('class','mt-3 row g-3 align-items-center');
									$('#appointment-hour-select').empty();
									for(let i=data.fromHour;i<data.toHour;i++){
										$('#appointment-hour-select').append($('<option>', {
										    value: i,
										    text: (i<10?"0"+i+":00":i+":00")
										}));
									}
									$('#request-appointment').removeAttr("disabled");	
								}
							},fail:function(){
								
							}
						});
					}else{
						$('#appoitnment-select-div').attr('class','mt-3 row g-3 align-items-center d-none');
						$('#appointment-hour-select').empty();
						alert("Schedule not available yet!");
					}
					if(data == null){
						$('#appoitnment-select-div').attr('class','mt-3 row g-3 align-items-center d-none');
						$('#appointment-hour-select').empty();
						alert("Schedule not available yet!");
						clearModal();
					}
				},
				fail: function(){
					$('#appoitnment-select-div').attr('class','mt-3 row g-3 align-items-center d-none');
					alert("Schedule search completed with fail!")
				}
			});
		});
		$('#request-appointment').click(function(){
			let date = $('#appointment-date').val();
			$.ajax({
				url:"appointment/find",
				method: "GET",
				data:{
					doctorId: doctor.id,
					date: $('#appointment-date').val()
				},
				success: function(data){
					console.log("already", data, data.length, Object.keys(data).length);
					if(Object.keys(data).length != 0){
						alert("You already have an appointment on "+data.date+" at "+(data.hour<10?"0"+data.hour+":00":data.hour+":00"));
						clearModal();
					}
					else{
						$.ajax({
							url:"appointment/add",
							method: "POST",
							data:{
								doctorId: doctor.id,
								date: $('#appointment-date').val(),
								hour: $('#appointment-hour-select').val(),
								condition: $('#condition').val()
							},
							success: function(data){
								clearModal();
								$('#bookAppointmentModal').modal('hide');
								if(data != null || data != ""){
									alert("Appointment request has been sent successfully");
								}
								else{
									alert("Data is null");
								}
							},
							fail: function(){
								alert("Appointment request failed!");
							}
						});
					}
				},
				fail: function(){
					alert("Appointment request failed");
				}
			});
		});
		clearModal();
		$('#request-appointment').attr('disabled', 'true');
	});
	
    return $template;
}

function getAllDoctors(){
	$.ajax({
		url:"/doctors/all",
		method:"GET",
		success: function(data){
			doctors = data;
			renderDoctorsList();
		},
		fail: function(){
			alert("Request failed");
		}
	});
}

$('#find-doctors').click(function(){
    $('#search-doctor-results-card').attr('class', 'card mt-2 mt-md-0');
	$('#hide-search-doctors-results').attr('class', 'btn btn-primary w-25 d-inline');
	let location = $('#search-doctor-location').val();
	let specialty = $('#search-doctor-specialty').val();
	data = {location: location, specialty: specialty};
	$.ajax({
		url:"doctors/search",
		method:"GET",
		data:{
			data: JSON.stringify(data)
		},
		success: function(data){
			clearSearchForm();
			searchDoctorsList = data;
			renderSearchDoctorsList();
		},
		fail: function(){
			alert("Search doctor temporarily not answering!");
		}
	});
});

$('#hide-search-doctors-results').click(function(){
    $('#search-doctor-results-card').attr('class', 'card mt-2 mt-md-0 d-none');
	$('#hide-search-doctors-results').attr('class', 'btn btn-primary w-25 d-inline d-none');
	clearSearchForm();
});

function clearSearchForm(){
	$('#search-doctor-location').val("");
	$('#search-doctor-specialty').val("");
}

function clearModal(){
	$('#appointment-date').val("");
	$('#appointment-hour-select').empty();
	$('#condition').val();
	
}

getAllDoctors();