<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<script src="http://code.jquery.com/jquery-1.9.1.min.js"></script>
	<title>Redis Page</title>
</head>
<body>
	<table>
		<tr>
			<td>User Name</td><td><input type="text" id = "userName" /></td>
		</tr>
		<tr>
			<td>Password</td><td><input type="text" id = "pass" /></td>
		</tr>
	</table>
	<button id="putBt">PUT</button>
	<button id="subBt">SUBSCRIBE</button>
	<div id="userSection"></div>
	<script type="text/javascript">
	$(document).ready(function (){
		 $.ajaxSetup({ cache: false });
		var listLoad = function() {
			$.getJSON('users', function(json){
				var users = json.users;
				var page = '';
				page += '<table>';
				page += '<tr>';
				page += 	'<td>Id</td><td>Name</td><td>Pub</td>';
				page += '</tr>';
				$.each(users, function(k, v){
					page += '<tr>';	
					page += 	'<td>' + v.id + '</td><td><a href="#" class="remove" key="'+v.id+'" >' + v.name + '</a></td><td><a href="#" class="pub" key="'+v.id+'" >pub</a></td>';	
					page += '</tr>';
				});
				page += '</table>';
				$('#userSection').html(page);
			});
		};
		$('#userSection').click(function(event){
			var target = event.target;
			if ($(target).hasClass('remove')){
				$.post('users/'+$(target).attr('key'), {'_method' : 'DELETE'}, function(json){
					listLoad();
				});
			} else if ($(target).hasClass('pub')){
				$.post('pub/'+$(target).attr('key'), function(json){
				});
			}
		});
		$('#putBt').click(function(){
			var params = {
				'_method' 	: 'PUT',
				name 		: $('#userName').val(),	
				pass 		: $('#pass').val()
			};
			$.post('users', params, function(json){
				listLoad();
			});
		});
		
		$('#subBt').click(function(){
			$.getJSON('sub', function(json){
			});
		});
		
		listLoad();
	});
	</script>
</body>
</html>
