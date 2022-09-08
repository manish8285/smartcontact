//console.log("script file working")

const toggleSidebar=()=>{
	if($(".sidebar").is(":visible")){
		$(".sidebar").css("display","none")
		$(".contentArea").css("margin-left","0%")
	}else{
		$(".sidebar").css("display","block")
		$(".contentArea").css("margin-left","20%")
	}
		
}

const viewRowNumber=(rows)=>{
	var rowNo = rows.value
	console.log(rowNo)
	
	$(document).ready(function(){
		$.ajax({
			url:"/user/view_contacts_row_no/"+rowNo
		})
	})
	
	location.reload()
	
}

const search_contact=()=>{
	//console.log("searching...")
	let query=$("#contact_search_input").val()
	if(query==""){
		$(".search-result").hide()
	}else{
		console.log(query)
		let url= `http://localhost:8080/contact/search/${query}`;
		
		fetch(url).then((rs)=>{
			return rs.json()
		}).then((data)=>{
			console.log(data)
			let rd = `<div class="list-group>"`;
			data.forEach((contact)=>{
				rd+=`<a href="/user/single_contact/${contact.id}" class="list-group-item"> ${contact.name} </a>`;
			});
			
			rd+=`</div>`
			$(".search-result").html(rd)
			$(".search-result").show()
		})
		
		//let result=`<a href="#">${query}`
		
		//result+=`</a>`
		
		
	}
	
	
	
}