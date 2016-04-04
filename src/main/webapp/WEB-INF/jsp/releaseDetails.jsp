<%@include file="includes/header.jsp"%>

<div class="row">
	<div class="col-lg-6">
		<div class="well text-left">
			<p>Below is a summary of the release.
			<h3>
				Click <a
					href="${releaseManager.releaseName}.xls?releaseName=${releaseManager.releaseName}">here</a>
				to generate an excel report.
			</h3>
			</p>
		</div>
	</div>
	<!-- /.col-lg-12 -->
</div>
<tr>
	<td>
		<table class="table">
			<tr>
				<td align="left">Release name : ${releaseManager.releaseName}</td>
			</tr>
			<tr>
				<td align="left">Release synopsis:
					${releaseManager.synopsisOfRelease}</td>
			</tr>
			<tr>
				<td align="left">Release nbr. linked DR:
					${releaseManager.numberOfLinkedDr}</td>
			</tr>
		</table>
	</td>
</tr>
<tr>
	<td><c:choose>
			<c:when test="${releaseManager.numberOfLinkedDr eq 0}">
				<table class="table">
					<thead>
						<tr>
							<td>Release details:</td>
						</tr>
						<tr>
							<td>Nr. of patches</td>
							<td>Nr. of transfer operation</td>
							<td>Nr. of manual operations</td>
							<td>Nr. of subjects</td>
						</tr>
						<tr>
							<td>${releaseManager.releaseName}</td>
							<td>${releaseManager.singleDeploymentRequest.numberOfPatches}</td>
							<td>${releaseManager.singleDeploymentRequest.numberOfTransferOperations}</td>
							<td>${releaseManager.singleDeploymentRequest.numberOfSubjects}</td>
						</tr>
					</thead>

				</table>
			</c:when>
			<c:otherwise>
				<table class="table">
					<thead>
					<c:forEach items="${releaseManager.linkedDeploymentRequest}" var="deploymentRequest">
						<tr>
							<td>${deploymentRequest.drName}</td>
						</tr>
						<tr>
							<td>Nr. of patches</td>
							<td>Nr. of transfer operation</td>
							<td>Nr. of manual operations</td>
							<td>Nr. of subjects</td>
						</tr>
						<tr>
							<td>${deploymentRequest.numberOfPatches}</td>
							<td>${deploymentRequest.numberOfTransferOperations}</td>
							<td>${deploymentRequest.numberOfManualTransferOperations}</td>
							<td>${deploymentRequest.numberOfSubjects}</td>
						</tr>
						</c:forEach>
					</thead>

				</table>
			</c:otherwise>
		</c:choose></td>
</tr>
</table>
<%@include file="includes/footer.jsp"%>
