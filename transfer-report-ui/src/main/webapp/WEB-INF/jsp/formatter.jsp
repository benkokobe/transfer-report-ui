<%@include file="includes/header.jsp"%>

   <form:form method="POST" commandName="user" enctype="multipart/form-data">
   
		<div class="btn btn-default btn-file">
			<label for="fileToFormat">Please select a file to format : </label> 
			   <!-- <input type="file" title="Search format" name="fileToFormat"  /> -->
			   <form:input type="file" path="fileToFormat" class="filestyle" data-icon="false"  name="fileToFormat"/>
			   <form:errors path="fileToFormat" cssStyle="color: #ff0000;" />
		</div>
		<div class="form-group">
			<button type="submit" class="btn btn-default">Format</button>
		</div>
	</form:form>

<%@include file="includes/footer.jsp"%>