let schedule = []
let searchSchedule = []
function renderScheduleList(){
    $scheduleList = $('#working-schedule-list');
    $scheduleList.empty();
    if(schedule == null || schedule.length == 0){
        $('#results-to-show').removeAttr('class');
    }else{
        $('#results-to-show').attr('class','d-none');
        schedule.forEach(item => {
            const $template = getScheduleTemplate(item);
            $scheduleList.append($template);
        });
    }
}

function getScheduleTemplate(item){
    const $template = $($('#schedule-template').html());
    $template.find('#result-date').text(item.date);
    $template.find('#from-hour').text(item.fromHour);
    $template.find('#to-hour').text(item.toHour);
    $template.find('#edit-schedule').click(function(){
	    $('#edit-date').val(item.date);
	    $('#edit-from').val(item.fromHour);
	    $('#edit-to').val(item.toHour);
		$('#edit-schedule-modal-btn').click(function(){
			console.log("edit-date",$('#edit-date').val());
			console.log("edit-from",$('#edit-from').val());
			console.log("edit-to",$('#edit-to').val());
			$.ajax({
				url:"schedule/edit",
				method:"POST",
				data:{
					date: $('#edit-date').val(),
					fromHour: $('#edit-from').val(),
					toHour: $('#edit-to').val()
				},
				success: function(data){
					console.log(data);
					if(data != null){
						getAllScheduleInfo();
						$searchList = $('#search-schedule-list');
    					$searchList.empty();
						$("#editScheduleModal").modal('hide');
					}else{
						alert("Try again later");
					}
				},
				fail:function(){
					alert("Edit schedule failed");
				}
			});
		});
	});
    $template.find('#remove-schedule').click(function(){
		let id = item.id;
		$.ajax({
			url:"schedule/delete",
			method: "DELETE",
			data:{
				id:id
			},
			success:function(data){
				if(data == true){
					getAllScheduleInfo();
					$searchList = $('#search-schedule-list');
    				$searchList.empty();
				}else{
					window.location.href="index.html";
				}
			},
			fail:function(){
				alert("Request failed!");
			}
		});
	});
    return $template;
}

function getAllScheduleInfo(){
	$.ajax({
		url:"/schedule",
		method:"GET",
		success: function(data){
			schedule = data;
			renderScheduleList();
		},
		fail: function(){
			alert("Request failed");
		}
	});
}

function renderSearchScheduleItem(){
    $searchList = $('#search-schedule-list');
    $searchList.empty();
    if(searchSchedule == null || searchSchedule.length == 0){
        $('#search-results-to-show').removeAttr('class');
    }else{
        $('#search-results-to-show').attr('class','d-none');
		searchSchedule.forEach(item => {
		            const $template = getScheduleTemplate(item);
		            $searchList.append($template);
		});
    }
}

$('#add-schedule').click(function(){
    let date = $('#schedule-date').val();
    let fromHour = $('#schedule-from').val();
    let toHour = $('#schedule-to').val();
	$.ajax({
		url:"schedule/add",
		method: "POST",
		data:{
			date: date,
			fromHour: fromHour,
			toHour: toHour
		},
		success: function(data){
			getAllScheduleInfo();
			clearForm();
		},
		fail: function(){
			alert("Request cannot be executed");
		}
	});
});

$("#schedule-from").bind('keyup mouseup', function () {
    $("#schedule-to").attr('min', (parseInt($("#schedule-from").val())+1));
});

$('#get-working-hours').click(function(){
    $('#search-results-card').attr('class', 'card mt-2 mt-md-0');
	$('#hide-search').attr('class', 'btn btn-primary ms-2 d-inline');
	let date = $('#search-date').val();
	console.log(date);
	$.ajax({
		url:"schedule/search",
		method:"GET",
		data:{
			date:date
		},
		success: function(data){
			console.log(data);
			searchSchedule = data;
			renderSearchScheduleItem();
		},
		fail: function(){
			alert("Request failed");
		}
	});
});

$('#hide-search').click(function(){
    $('#search-results-card').attr('class', 'card mt-2 mt-md-0 d-none');
	$('#hide-search').attr('class', 'btn btn-primary ms-2 d-inline d-none');
});

function clearForm(){
    $('#schedule-date').val("");
	$('#schedule-from').val("");
    $('#schedule-to').val("");
}

getAllScheduleInfo();