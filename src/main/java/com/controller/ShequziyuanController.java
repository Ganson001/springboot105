package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.ShequziyuanEntity;
import com.entity.view.ShequziyuanView;

import com.service.ShequziyuanService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 社区资源
 * 后端接口
 * @author 
 * @email 
 * @date 2021-05-08 01:51:09
 */
@RestController
@RequestMapping("/shequziyuan")
public class ShequziyuanController {
    @Autowired
    private ShequziyuanService shequziyuanService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ShequziyuanEntity shequziyuan,
		HttpServletRequest request){
        EntityWrapper<ShequziyuanEntity> ew = new EntityWrapper<ShequziyuanEntity>();
		PageUtils page = shequziyuanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shequziyuan), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ShequziyuanEntity shequziyuan, 
		HttpServletRequest request){
        EntityWrapper<ShequziyuanEntity> ew = new EntityWrapper<ShequziyuanEntity>();
		PageUtils page = shequziyuanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shequziyuan), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ShequziyuanEntity shequziyuan){
       	EntityWrapper<ShequziyuanEntity> ew = new EntityWrapper<ShequziyuanEntity>();
      	ew.allEq(MPUtil.allEQMapPre( shequziyuan, "shequziyuan")); 
        return R.ok().put("data", shequziyuanService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ShequziyuanEntity shequziyuan){
        EntityWrapper< ShequziyuanEntity> ew = new EntityWrapper< ShequziyuanEntity>();
 		ew.allEq(MPUtil.allEQMapPre( shequziyuan, "shequziyuan")); 
		ShequziyuanView shequziyuanView =  shequziyuanService.selectView(ew);
		return R.ok("查询社区资源成功").put("data", shequziyuanView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ShequziyuanEntity shequziyuan = shequziyuanService.selectById(id);
		shequziyuan.setClicknum(shequziyuan.getClicknum()+1);
		shequziyuan.setClicktime(new Date());
		shequziyuanService.updateById(shequziyuan);
        return R.ok().put("data", shequziyuan);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ShequziyuanEntity shequziyuan = shequziyuanService.selectById(id);
		shequziyuan.setClicknum(shequziyuan.getClicknum()+1);
		shequziyuan.setClicktime(new Date());
		shequziyuanService.updateById(shequziyuan);
        return R.ok().put("data", shequziyuan);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ShequziyuanEntity shequziyuan, HttpServletRequest request){
    	shequziyuan.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shequziyuan);
        shequziyuanService.insert(shequziyuan);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ShequziyuanEntity shequziyuan, HttpServletRequest request){
    	shequziyuan.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shequziyuan);
        shequziyuanService.insert(shequziyuan);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ShequziyuanEntity shequziyuan, HttpServletRequest request){
        //ValidatorUtils.validateEntity(shequziyuan);
        shequziyuanService.updateById(shequziyuan);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        shequziyuanService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<ShequziyuanEntity> wrapper = new EntityWrapper<ShequziyuanEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}


		int count = shequziyuanService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,ShequziyuanEntity shequziyuan, HttpServletRequest request,String pre){
        EntityWrapper<ShequziyuanEntity> ew = new EntityWrapper<ShequziyuanEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicknum");
        params.put("order", "desc");
		PageUtils page = shequziyuanService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shequziyuan), params), params));
        return R.ok().put("data", page);
    }


}
