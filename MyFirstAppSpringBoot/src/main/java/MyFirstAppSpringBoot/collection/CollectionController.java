package MyFirstAppSpringBoot.collection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CollectionController {

    private ArrayList<String> arrayList;
    private HashMap<Integer, String> hashMap;

    @GetMapping("/update-array")
    public void updateArrayList(@RequestParam("s") String s) {
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        arrayList.add(s);
    }

    @GetMapping("/show-array")
    public List<String> showArrayList() {
        return arrayList == null ? List.of() : arrayList;
    }

    @GetMapping("/update-map")
    public void updateHashMap(@RequestParam("s") String s) {
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        hashMap.put(hashMap.size(), s);
    }

    @GetMapping("/show-map")
    public Map<Integer, String> showHashMap() {
        return hashMap == null ? Map.of() : hashMap;
    }

    @GetMapping("/show-all-lenght")
    public String showAllLenght() {
        int arraySize = arrayList == null ? 0 : arrayList.size();
        int mapSize = hashMap == null ? 0 : hashMap.size();
        return "Количество элементов в ArrayList: " + arraySize
                + ", количество элементов в HashMap: " + mapSize;
    }
}
