package blogsystem.controller;

import blogsystem.dto.AdDTO;
import blogsystem.vo.Result;
import blogsystem.entity.Ad;
import blogsystem.entity.AdType;
import blogsystem.service.IAdService;
import blogsystem.service.IAdTypeService;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/ad")
@RequiredArgsConstructor
@Tag(name = "广告管理接口", description = "提供广告和广告类型的管理功能")
public class AdController {
    private final IAdService adService;
    private final IAdTypeService adTypeService;

    @Operation(summary = "获取广告列表", description = "根据广告类型ID获取对应的广告列表")
    @GetMapping("/list")
    public Result getAdList(@RequestParam String adTypeId ){
        List<AdDTO> adList = adService.getAdList(adTypeId);;
        if (adList.isEmpty()) {
            return Result.error("没有广告");
        } else {
            return Result.success(adList);
        }
    }

    @Operation(summary = "获取广告详情", description = "根据广告ID获取广告的详细信息")
    @PostMapping("/add")
    public Result addAd(@RequestBody AdDTO adDto) {
        boolean isAdded = adService.add(adDto);
        if (isAdded) {
            return Result.success("添加成功");
        } else {
            return Result.error("添加失败");
        }
    }

    @Operation(summary = "编辑广告", description = "根据广告ID更新广告信息")
    @PutMapping("/edit")
    public Result editAd(@RequestBody AdDTO adDto) throws ClientException {
       return adService.updateById(adDto);
    }

    @Operation(summary = "删除广告", description = "根据广告ID删除广告")
    @DeleteMapping
    public Result deleteAd(@RequestParam String adId) throws ClientException {
        boolean isDeleted = adService.delete(adId);
        if (isDeleted) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    @Operation(summary = "获取广告类型列表", description = "获取所有广告类型的列表")
    @GetMapping("/type/list")
    public Result getAdTypeList() {
        QueryWrapper<AdType> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("ad_type_sort");
        List<AdType> adTypeList = adTypeService.list(queryWrapper);
        if (adTypeList.isEmpty()) {
            return Result.error("No ad types found");
        }
        return Result.success(adTypeList);
    }

    @Operation(summary = "获取广告类型详情", description = "根据广告类型ID获取广告类型的详细信息")
    @PostMapping("/type/add")
    public Result addAdType(@RequestBody AdType adType) {
        boolean isAdded = adTypeService.add(adType);
        if (isAdded) {
            return Result.success("广告类型添加成功");
        } else {
            return Result.error("广告类型添加失败");
        }
    }

    @Operation(summary = "编辑广告类型", description = "根据广告类型ID更新广告类型信息")
    @PutMapping("/type/edit")
    public Result editAdType(@RequestBody AdType adType) {
        boolean isUpdated = adTypeService.updateById(adType);
        if (isUpdated) {
            return Result.success("广告类型更新成功");
        } else {
            return Result.error("广告类型更新失败");
        }
    }

    @Operation(summary = "删除广告类型", description = "根据广告类型ID删除广告类型")
    @DeleteMapping("/type")
    public Result deleteAdType(@RequestParam String adTypeId) {
        boolean isDeleted = adTypeService.deleteById(adTypeId);
        if (isDeleted) {
            return Result.success("广告类型删除成功");
        } else {
            return Result.error("广告类型删除失败");
        }
    }
}
