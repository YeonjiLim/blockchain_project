
package com.bcauction.api;


import com.bcauction.application.IDigitalWorkService;
import com.bcauction.application.IFabricService;
import com.bcauction.domain.DigitalWork;
import com.bcauction.domain.FabricAsset;
import com.bcauction.domain.exception.EmptyListException;
import com.bcauction.domain.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class DigitalWorkController
{
	public static final Logger logger = LoggerFactory.getLogger(DigitalWorkController.class);

	private IDigitalWorkService digitalWorkService;
	private IFabricService fabricService;

	@Autowired
	public DigitalWorkController(IDigitalWorkService digitalWorkService,
	                             IFabricService fabricService) {
		Assert.notNull(digitalWorkService, "digitalWorkService 개체가 반드시 필요!");
		Assert.notNull(fabricService, "fabricService 개체가 반드시 필요!");
		this.digitalWorkService = digitalWorkService;
		this.fabricService = fabricService;
	}

	@RequestMapping(value = "/works", method = RequestMethod.POST)
	public DigitalWork register(@RequestBody DigitalWork work) {
		System.out.println(work.toString());
		return  digitalWorkService.registerItem(work);
	}


	@RequestMapping(value = "/works", method = RequestMethod.GET)
	public List<DigitalWork> checkList() {
		List<DigitalWork> list = digitalWorkService.checkList();
		if (list == null || list.isEmpty())
			throw new EmptyListException("NO DATA");

		return list;
	}

	@RequestMapping(value = "/works/{id}", method = RequestMethod.GET)
	public DigitalWork search(@PathVariable int id) {
		DigitalWork item = digitalWorkService.search(id);
		if (item == null) {
			logger.error("NOT FOUND ID: ", id);
			throw new NotFoundException(id + " 작품 정보를 찾을 수 없습니다.");
		}

		return item;
	}

	@RequestMapping(value = "/works", method = RequestMethod.PUT)
	public DigitalWork update(@RequestBody DigitalWork work) {
		System.out.println(work+"확인");
		DigitalWork updated_item = digitalWorkService.updateItemInfo(work);
		if (updated_item == null) {
			logger.error("NOT FOUND WORK ID: ", work.getId());
			throw new NotFoundException(work.getId() + " 작품 정보를 찾을 수 없습니다.");
		}

		return updated_item;
	}

	@RequestMapping(value = "/works/{id}", method = RequestMethod.DELETE)
	public DigitalWork delete(@PathVariable int id) {
		return  digitalWorkService.deleteItem(id);
	}


	// TODO : not need to use the function below.
	/**
	 * 협업과제
	 * week. 4-7
	 * mission. 3
	 * Req. 1-1
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/works/owner/{id}", method = RequestMethod.GET)
	public List<DigitalWork> memberItemCheckList(@PathVariable int id){
		List<DigitalWork> list = digitalWorkService.memberItemCheckList(id);
		//System.out.println("AAA"+list);
		if (list == null || list.isEmpty() )
			throw new EmptyListException("사용자 소유의 작품이 없습니다.");

		return list;
	}

	/**
	 * 협업과제
	 * 협업과제
	 * week. 4-7
	 * mission. 3
	 * Req. 1-2
	 */
	public List<FabricAsset> searchItemHistory(@PathVariable int id){
		List<FabricAsset> history = this.fabricService.searchItemHistory(id);
		if(history == null || history.isEmpty())
			throw new EmptyListException("search된 작품 이력이 없습니다.");

		return history;
	}

}
