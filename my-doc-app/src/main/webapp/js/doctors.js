let doctors = []
let searchDoctorsList = []

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

getAllDoctors();