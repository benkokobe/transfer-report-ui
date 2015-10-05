package com.bko.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.domain.TransferOperation;
import com.bko.persistence.DeploymentRequestDao;





public class DeploymentRequestServiceImpl implements DeploymentRequestService{
		
	private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentRequestServiceImpl.class);
	
	private DeploymentRequestDao deploymentRequestDao;

	public void setDeploymentRequestDao(DeploymentRequestDao deploymentRequestDao) {
		this.deploymentRequestDao = deploymentRequestDao;
	}

	public List<Patch> getPatchList(String deploymentRequest) {
		
		return this.deploymentRequestDao.getPatchList(deploymentRequest);
	}

	/*
	 * public List<Patch> getPatchListComplete(String NAMLOT) {
		return this.deploymentRequestDao.getPatchListComplete(NAMLOT);
	}
	*/

	public List<TransferOperation> getTransferOperation(String deploymentRequest) {
		return this.deploymentRequestDao.getTransferOperation(deploymentRequest);
	}


	public String getRefLot(String drName) {
		return getRefLot(drName);
	}

	public List<TransferOperation> getMissingYe(String reflot) {
		return getMissingYe(reflot);
	}

	@Override
	public List<PatchMember> getDRMembers(String drName) {
		return this.deploymentRequestDao.getDRMembers(drName);
	}

	@Override
	public int getNumberOfPatches(String deploymentRequestName) {
		return this.deploymentRequestDao.getNumberOfPatches(deploymentRequestName);
	}

	@Override
	public int getnumberOfTransferOperations(String deploymentRequestName) {
		return this.deploymentRequestDao.getnumberOfTransferOperations(deploymentRequestName);
	}

}
