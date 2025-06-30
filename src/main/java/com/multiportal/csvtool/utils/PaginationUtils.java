package com.multiportal.csvtool.utils;

import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

public class PaginationUtils {

    public static <T> List<T> paginate(List<T> items, int page, int pageSize, Model model) {

//        if (items == null || items.isEmpty()) {
//            model.addAttribute("rows", Collections.emptyList());
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", 1);
//            return Collections.emptyList();
//        }


        var results =  allowsPaginate(items, page, model);
        if(results.isEmpty()) return results;

        int totalItem = items.size();
        int totalPages = (int) Math.ceil((double) totalItem / pageSize);
        int maxDisplayPages = 10;
        int startPage = ((page - 1) / maxDisplayPages) * maxDisplayPages + 1;
        int endPage = Math.min(startPage + maxDisplayPages - 1, totalPages);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        if(page < 1) page = 1;
        if(page > totalPages) page = totalPages;

        int fromIndex = (page - 1 ) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalItem);

        List<T> paginated = items.subList(fromIndex, toIndex);

        model.addAttribute("rows", paginated);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return paginated;
    }

    private static <T> List<T> allowsPaginate(List<T> items, int page, Model model){
        //checks if list of items are null or empty
        if(items == null || items.isEmpty()) {
            model.addAttribute("rows", Collections.emptyList());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", 1);
            return Collections.emptyList();
        }
        return items;
    }

}
