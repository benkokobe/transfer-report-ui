package com.bko.service;

import java.util.List;

import com.bko.domain.PatchTask;
import com.bko.persistence.PatchTaskDao;

public class PatchTaskServiceImpl implements PatchTaskService{
	
	public void setPatchTaskDao(PatchTaskDao patchTaskDao) {
		this.patchTaskDao = patchTaskDao;
	}

	private PatchTaskDao patchTaskDao;

	public List<PatchTask> getPatchId(String taskId) {
		return patchTaskDao.getPatchId(taskId);
	}

}
