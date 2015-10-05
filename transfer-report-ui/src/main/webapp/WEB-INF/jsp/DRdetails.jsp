<%@include file="includes/header.jsp"%>	

<table class="table table-bordered">
<caption>Deployment Request details</caption>
	<tr><td>
		<table>
			<tr>
				<td>DR name:</td>
				<td>${deploymentRequest.drName}</td>
			</tr>
			<tr>
				<td>DR synopsis:</td>
				<td>${deploymentRequest.drName}</td>
			</tr>
		</table>
	</td></tr>
	<tr><td>
		<table>
			<thead>
				<tr>
					<td>DR details:</td>
				</tr>
				<tr>
					<td>Nr. of patches</td>
					<td>Nr. transfer operation</td>
					<td>Nr. manual operations</td>
				</tr>
				<tr>
				    <td>${deploymentRequest.numberOfPatches}</td>
					<td>${deploymentRequest.numberOfTransferOperations}</td>
					<td>${deploymentRequest.drName}</td>
				</tr>
			</thead>

		</table>
	</td></tr>
</table>
Click <a href="generate.xls?drName=${deploymentRequest.drName}">here</a> to generate an excel report.

<%@include file="includes/footer.jsp"%>
