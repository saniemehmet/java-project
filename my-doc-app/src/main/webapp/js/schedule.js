let schedule = []

function renderScheduleList(){
    $scheduleList = $('#working-schedule-list');
    $scheduleList.empty();
    if(schedule == null || schedule.length == 0){
	console.log("v tozi if");
        $('#results-to-show').removeAttr('class');
    }else{
        $('#results-to-show').attr('class','d-none');
        schedule.forEach(item => {
            console.log("item",item);
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
    return $template;
}

function getAllScheduleInfo(){
	$.ajax({
		url:"/schedule",
		method:"GET",
		success: function(data){
			console.log(data);
			schedule = data;
			console.log(schedule);
			renderScheduleList();
		},
		fail: function(){
			alert("Request failed");
		}
	});
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
   // schedule.push({date:date, from:from, to:to});
   // renderScheduleList();
    console.log(schedule);
});

function clearForm(){
    $('#schedule-date').val("");
	$('#schedule-from').val("");
    $('#schedule-to').val("");
}

getAllScheduleInfo();