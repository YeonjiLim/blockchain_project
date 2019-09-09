package com.bcauction.application.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bcauction.application.IDigitalWorkService;
import com.bcauction.application.IFabricCCService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.exception.ApplicationException;
import com.bcauction.domain.repository.IDigitalWorkRepository;
import com.bcauction.domain.repository.IOwnershipRepository;

@Service
public class DigitalWorkService implements IDigitalWorkService
{
	public static final Logger logger = LoggerFactory.getLogger(DigitalWorkService.class);

	private IDigitalWorkRepository digitalWorkRepository;
	private IFabricService fabricService;

	@Autowired
	public DigitalWorkService(IFabricService fabricService,
	                          IDigitalWorkRepository digitalWorkRepository) {
		this.fabricService = fabricService;
		this.digitalWorkRepository = digitalWorkRepository;
	}

	@Override
	public List<DigitalWork> checkList()
	{
		return this.digitalWorkRepository.checkList();
	}

	@Override
	public List<DigitalWork> memberItemCheckList(final long id)
	{
		return this.digitalWorkRepository.memberItemCheckList(id);
	}

	@Override
	public DigitalWork search(final long id)
	{
		return this.digitalWorkRepository.search(id);
	}

	/**
	 * item 등록 시 item 정보를 저장하고
	 * 패브릭에 item 소유권을 등록한다.
	 * @param item
	 * @return DigitalWork
	 */
	@Override
	public DigitalWork registerItem(final DigitalWork item) {
		// TODO.
		digitalWorkRepository.add(item);
		DigitalWork dw=digitalWorkRepository.search(item.getMember_id(),item.getName());
		System.out.println(dw+"확인용");
		fabricService.registerPossession(dw.getMember_id(), dw.getId());
		return dw;
	}

	/**
	 * item 삭제 시, item의 상태를 업데이트하고
	 * 패브릭에 item 소유권 소멸 이력을 추가한다.
	 * @param id itemid
	 * @return DigitalWork
	 */
	@Override
	public DigitalWork deleteItem(final long id)
	{
		// TODO
		DigitalWork dw=digitalWorkRepository.search(id);
		dw.setStatus("N");
		digitalWorkRepository.update(dw);
		fabricService.expirePossession(dw.getMember_id(), dw.getId());
		digitalWorkRepository.delete(id);
		return dw;
	}

	@Override
	public DigitalWork updateItemInfo(final DigitalWork item) {
		DigitalWork workStored = this.digitalWorkRepository.search(item.getId());
		if (workStored == null)
			throw new ApplicationException("해당 item을 찾을 수 없습니다.");

		if (item.getMember_id() != 0 && item.getMember_id() != workStored.getMember_id())
			throw new ApplicationException("잘못된 접근입니다.");

		if(item.getName() == null || "".equals(item.getName()))
			item.setName(workStored.getName());
		if(item.getExplanation() == null || "".equals(item.getExplanation()))
			item.setExplanation(workStored.getExplanation());
		if(item.getDisclosure() == null || "".equals(item.getDisclosure()))
			item.setDisclosure(workStored.getDisclosure());
		if(item.getStatus() == null || "".equals(item.getStatus()))
			item.setStatus(workStored.getStatus());
		if(item.getMember_id() == 0)
			item.setMember_id(workStored.getMember_id());

		int affected = this.digitalWorkRepository.update(item);
		if(affected == 0)
			throw new ApplicationException("item정보수정 처리가 반영되지 않았습니다.");

		return item;
	}


}
