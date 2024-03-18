package com.TourismApp.TourismApplication.Controller;

import com.TourismApp.TourismApplication.Models.Item;
import com.TourismApp.TourismApplication.Services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/items")

public class ItemController {
    @Autowired
    private final ItemService itemService;
    @Autowired
    public ItemController(ItemService itemService){
        this.itemService=itemService;
    }

    @GetMapping("/gelAllItems")
    public ResponseEntity<List<Item>> getAllUItems() {
        List<Item> items = itemService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
    @GetMapping("/getItemsByIds")
    public ResponseEntity<List<Item>> getItemsByIds(@RequestParam List<String> itemIds) {
        List<Item> items = itemService.getItemsByIds(itemIds);

        if (!items.isEmpty()) {
            return new ResponseEntity<>(items, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/item/{idItem}")
    public ResponseEntity<Item> getItemById(@PathVariable String idItem) {
        Optional<Item> item = itemService.getItemById(idItem);
        return item.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/createItem")
    public Item createItems(@RequestBody Item item) {
        return itemService.createItem(item);
    }

    @PutMapping("/updateitem/{idItem}")
    public ResponseEntity<Item> updateItemById(@PathVariable String idItem, @RequestBody Item updatedItem) {
        return itemService.updateItemById(idItem, updatedItem);
    }

    @PutMapping("archiveItem/{idItem}")
    public ResponseEntity<Item> archiveitem(@PathVariable String idItem){
        boolean success = itemService.archiveItem(idItem);
        if(success){
            return  ResponseEntity.ok().build();
        }
        else {
            return  ResponseEntity.notFound().build();
        }
    }

    @GetMapping("isarchiveItem")
    public ResponseEntity<List<Item>> findArchiveItems(){
        List<Item> archivedItems = itemService.findArchivedItem();
        return ResponseEntity.ok(archivedItems);
    }

    @DeleteMapping("/deleteItem/{idItem}")
    public ResponseEntity<Void> deleteItem(@PathVariable String idItem) {
        itemService.deleteItem(idItem);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/nonDeletedItems")
    public List<Item> getNonDeletedItems() {
        return itemService.getNonDeletedItems();
    }

}
