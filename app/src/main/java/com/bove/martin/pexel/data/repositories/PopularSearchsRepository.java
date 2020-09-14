package com.bove.martin.pexel.data.repositories;

import androidx.lifecycle.MutableLiveData;

import com.bove.martin.pexel.data.model.Search;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Martín Bove on 12-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public class PopularSearchsRepository {
    private static PopularSearchsRepository instance;

    public static PopularSearchsRepository getInstance() {
        if(instance == null) {
            instance = new PopularSearchsRepository();
        }
        return instance;
    }

    public MutableLiveData<List<Search>> getSearchs() {
        MutableLiveData<List<Search>> mData = new MutableLiveData<>();
        ArrayList<Search> randomOrderSearch = populateSearchList();
        Collections.shuffle(randomOrderSearch);
        //randomOrderSearch.add(0, new Search("pexels","Fotos del Día", null));
        mData.setValue(randomOrderSearch);
        return mData;
    }

    private ArrayList<Search> populateSearchList() {
        ArrayList<Search> mSearchs = new ArrayList<>();

        mSearchs.add(new Search("desk","escritorio", "https://images.pexels.com/photos/6471/woman-hand-smartphone-desk.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("writing","escribir", "https://images.pexels.com/photos/210661/pexels-photo-210661.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("coffee","café", "https://images.pexels.com/photos/34079/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("desktop wallpaper","desktop wallpaper", "https://images.pexels.com/photos/14676/pexels-photo-14676.png?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("cooking","cocinar", "https://images.pexels.com/photos/8717/food-pot-kitchen-cooking.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("person","persona", "https://images.pexels.com/photos/91224/pexels-photo-91224.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("vacation","vacaciones", "https://images.pexels.com/photos/386009/pexels-photo-386009.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("universe","universo", "https://images.pexels.com/photos/2154/sky-lights-space-dark.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("river","rio", "https://images.pexels.com/photos/58557/pexels-photo-58557.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("communication","comunicación", "https://images.pexels.com/photos/362/marketing-man-person-communication.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("office","oficina", "https://images.pexels.com/photos/36990/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("wood","madera", "https://images.pexels.com/photos/172289/pexels-photo-172289.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("money","dinero", "https://images.pexels.com/photos/36104/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("baby","bebe", "https://images.pexels.com/photos/161709/newborn-baby-feet-basket-161709.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("student","estudiante", "https://images.pexels.com/photos/8769/pen-writing-notes-studying.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("clouds","nubes", "https://images.pexels.com/photos/53594/blue-clouds-day-fluffy-53594.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("sky","cielo", "https://images.pexels.com/photos/55787/pexels-photo-55787.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("woman","mujer", "https://images.pexels.com/photos/38554/girl-people-landscape-sun-38554.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("dance","baile", "https://images.pexels.com/photos/12312/pexels-photo-12312.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("city","ciudad", "https://images.pexels.com/photos/169647/pexels-photo-169647.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("beach","playa", "https://images.pexels.com/photos/17727/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("team","equipo", "https://images.pexels.com/photos/2209/people-men-grass-sport.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("blur","blur", "https://images.pexels.com/photos/8395/lights-night-unsharp-blured.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("fire","fuego", "https://images.pexels.com/photos/1749/fire-orange-emergency-burning.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("food","comida", "https://images.pexels.com/photos/5317/food-salad-restaurant-person.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("flowers","flores", "https://images.pexels.com/photos/909/flowers-garden-colorful-colourful.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("music","musica", "https://images.pexels.com/photos/164821/pexels-photo-164821.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("school","escuela", "https://images.pexels.com/photos/207665/pexels-photo-207665.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("art","arte", "https://images.pexels.com/photos/19680/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("light","luz", "https://images.pexels.com/photos/45072/pexels-photo-45072.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("couple","pareja", "https://images.pexels.com/photos/18397/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("study","estudio", "https://images.pexels.com/photos/6972/summer-office-student-work.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("HD wallpaper","HD wallpaper", "https://images.pexels.com/photos/87646/horsehead-nebula-dark-nebula-constellation-orion-87646.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("home","casa", "https://images.pexels.com/photos/2459/stairs-home-loft-lifestyle.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("vintage","vintage", "https://images.pexels.com/photos/247929/pexels-photo-247929.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("heart","corazón", "https://images.pexels.com/photos/5390/sunset-hands-love-woman.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("phone","teléfono", "https://images.pexels.com/photos/40011/iphone-smartphone-apps-apple-inc-40011.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("grass","pasto", "https://images.pexels.com/photos/54567/pexels-photo-54567.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("mountains","montañas", "https://images.pexels.com/photos/93684/pexels-photo-93684.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("internet","internet", "https://images.pexels.com/photos/6335/man-coffee-cup-pen.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("paint","pintura", "https://images.pexels.com/photos/102127/pexels-photo-102127.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("smile","sonrisa", "https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("sport","deporte", "https://images.pexels.com/photos/317157/pexels-photo-317157.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("family","familia", "https://images.pexels.com/photos/8509/family.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("notebook","notebook", "https://images.pexels.com/photos/68562/pexels-photo-68562.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("technology","tecnología", "https://images.pexels.com/photos/35550/ipad-tablet-technology-touch.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("new york city wallpaper","new york city wallpaper", "https://images.pexels.com/photos/7837/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("finance","finanzas", "https://images.pexels.com/photos/50987/money-card-business-credit-card-50987.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("training","entrenamiento", "https://images.pexels.com/photos/28080/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("working","trabajar", "https://images.pexels.com/photos/7374/startup-photos.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("nature","naturaleza", "https://images.pexels.com/photos/60006/spring-tree-flowers-meadow-60006.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("marketing","marketing", "https://images.pexels.com/photos/33999/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("architecture","arquitectura", "https://images.pexels.com/photos/27406/pexels-photo-27406.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("holiday","feriado", "https://images.pexels.com/photos/6526/sea-beach-holiday-vacation.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("tools","herramientas", "https://images.pexels.com/photos/175039/pexels-photo-175039.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("camera","cámara", "https://images.pexels.com/photos/122400/pexels-photo-122400.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("meeting","reunion", "https://images.pexels.com/photos/7097/people-coffee-tea-meeting.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("night","noche", "https://images.pexels.com/photos/53153/full-moon-moon-night-sky-53153.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("water","agua", "https://images.pexels.com/photos/36487/above-adventure-aerial-air.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("adventure","aventura", "https://images.pexels.com/photos/287240/pexels-photo-287240.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("old","anciano", "https://images.pexels.com/photos/33786/hands-walking-stick-elderly-old-person.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("paper","papel", "https://images.pexels.com/photos/6372/coffee-smartphone-desk-pen.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("building","construcción", "https://images.pexels.com/photos/27406/pexels-photo-27406.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("desert","desierto", "https://images.pexels.com/photos/210307/pexels-photo-210307.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("computer","computadora", "https://images.pexels.com/photos/38568/apple-imac-ipad-workplace-38568.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("design","diseño", "https://images.pexels.com/photos/6224/hands-people-woman-working.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("mockup","mockup", "https://images.pexels.com/photos/39284/macbook-apple-imac-computer-39284.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("winter","invierno", "https://images.pexels.com/photos/54200/pexels-photo-54200.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("abstract","abstracto", "https://images.pexels.com/photos/8756/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("fitness","fitness", "https://images.pexels.com/photos/1229356/pexels-photo-1229356.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("relax","relax", "https://images.pexels.com/photos/51397/legs-window-car-dirt-road-51397.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("crowd","multitud", "https://images.pexels.com/photos/9816/pexels-photo-9816.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("mobile","móvil", "https://images.pexels.com/photos/4831/hands-coffee-smartphone-technology.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("rain","lluvia", "https://images.pexels.com/photos/21492/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("construction","construcción", "https://images.pexels.com/photos/2489/street-building-construction-industry.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("group","grupo", "https://images.pexels.com/photos/9746/people-mother-family-father.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("books","libros", "https://images.pexels.com/photos/46274/pexels-photo-46274.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("party","fiesta", "https://images.pexels.com/photos/5156/people-eiffel-tower-lights-night.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("kids","niños", "https://images.pexels.com/photos/61129/pexels-photo-61129.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("portrait","retrato", "https://images.pexels.com/photos/4156/fashion-woman-model-portrait.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("sunset","atardecer", "https://images.pexels.com/photos/34014/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("face","cara", "https://images.pexels.com/photos/51969/model-female-girl-beautiful-51969.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("hair","cabello", "https://images.pexels.com/photos/54566/pexels-photo-54566.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("young","joven", "https://images.pexels.com/photos/35537/child-children-girl-happy.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("finance","finanzas", "https://images.pexels.com/photos/50987/money-card-business-credit-card-50987.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("fun","diversión", "https://images.pexels.com/photos/40815/youth-active-jump-happy-40815.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("smartphone","smartphone", "https://images.pexels.com/photos/7423/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("animals","animales", "https://images.pexels.com/photos/52500/horse-herd-fog-nature-52500.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("apple","manzana", "https://images.pexels.com/photos/39803/pexels-photo-39803.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("sad","tristeza", "https://images.pexels.com/photos/14303/pexels-photo-14303.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("summer","verano", "https://images.pexels.com/photos/46710/pexels-photo-46710.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("data","data", "https://images.pexels.com/photos/4316/technology-computer-chips-gigabyte.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("iphone","iphone", "https://images.pexels.com/photos/4322/person-woman-hand-space.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("car","automóvil", "https://images.pexels.com/photos/120049/pexels-photo-120049.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("plane","avión", "https://images.pexels.com/photos/59519/pexels-photo-59519.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("garden","jardín", "https://images.pexels.com/photos/102896/pexels-photo-102896.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("texture","textura", "https://images.pexels.com/photos/8892/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("tree","árbol", "https://images.pexels.com/photos/56875/tree-dawn-nature-bucovina-56875.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("landscape","paisaje", "https://images.pexels.com/photos/7919/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("africa","africa", "https://images.pexels.com/photos/33045/lion-wild-africa-african.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("friends","amigos", "https://images.pexels.com/photos/58592/pexels-photo-58592.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("business","negocios", "https://images.pexels.com/photos/7075/people-office-group-team.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("creative","creativo", "https://images.pexels.com/photos/7366/startup-photos.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("yoga","yoga", "https://images.pexels.com/photos/65977/pexels-photo-65977.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("fashion","fashion", "https://images.pexels.com/photos/6805/fashion-men-vintage-colorful.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("earth","tierra", "https://images.pexels.com/photos/2422/sky-earth-galaxy-universe.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("gift","regalo", "https://images.pexels.com/photos/360624/pexels-photo-360624.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("idea","idea", "https://images.pexels.com/photos/8704/pen-idea-bulb-paper.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("ice cream","helado", "https://images.pexels.com/photos/8374/food-ice-cream.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("medical","medico", "https://images.pexels.com/photos/4154/clinic-doctor-health-hospital.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("wedding","boda", "https://images.pexels.com/photos/1033/man-couple-people-woman.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("time","tiempo", "https://images.pexels.com/photos/280264/pexels-photo-280264.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("black and white","blanco y negro", "https://images.pexels.com/photos/167964/pexels-photo-167964.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("man","hombre", "https://images.pexels.com/photos/736716/pexels-photo-736716.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("photography","fotografía", "https://images.pexels.com/photos/58625/pexels-photo-58625.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("job","trabajo", "https://images.pexels.com/photos/7375/startup-photos.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("sun","sol", "https://images.pexels.com/photos/189349/pexels-photo-189349.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("beauty","belleza", "https://images.pexels.com/photos/17737/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("girl","niña", "https://images.pexels.com/photos/160933/girl-rabbit-friendship-love-160933.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("map","mapa", "https://images.pexels.com/photos/21014/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("clothes","ropa", "https://images.pexels.com/photos/102129/pexels-photo-102129.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("model","modelo", "https://images.pexels.com/photos/37649/glamour-style-hat-woman-37649.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));
        mSearchs.add(new Search("social media","redes sociales", "https://images.pexels.com/photos/35177/pexels-photo.jpg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"));

        return mSearchs;
    }
}

