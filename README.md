# Coin Checker
Android application to check your favorite cryptocurrencies.

## Overview

The aim of this _school_ project was to get familiar with several technologies/libraries, and new features of the Android programming.
This project includes:
* MVP architecture
* Dagger2 dependency injection
* Retrofit2 REST client
* Travis CI
* Room persistence library
* Robolectric Unit testing
* Google Analytics, Firebase, Fabric, Crashlytics


### Class diagram
![Class diagram](https://i.imgur.com/tQHAuR4.png)

## Screens

### Main screen - Splash screen
As a main screen I implemented a **Splash screen**. It is just a UI thing, no effective pre-loading function implemented during the splash animation.

**splash.xml**
```
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- if your splash icon is too big, and not fit right, do not use padding hacks
     use 9-patch files instead, it is the only working solution -->
    <item android:drawable="@color/colorPrimary" />
    <item>
        <bitmap android:src="@drawable/coin_logo"
            android:gravity="center"/>

    </item>

</layer-list>
```

**styles.xml**
```
<resources>
.
.
.
    <style name="SplashTheme" parent="Theme.AppCompat.NoActionBar">
        <item name="android:windowBackground">@drawable/splash</item>
    </style>

</resources>
```

**activity_splash.xml**
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:theme="@style/SplashTheme">

</LinearLayout>
```

**MainActivity.java**
```
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        showCoins();
    }
    
    @Override
    public void showCoins() {
        final long splashScreenDuration = getSplashScreenDuration();

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("NavigateToCoinsScreen")
                .build());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, CoinsActivity.class);
                startActivity(intent);
                finish();
            }
        },splashScreenDuration);
    }

    private long getSplashScreenDuration() {return 2000L;}

```

This is the call sequence of the Splash screen, it is basically very simple.
1. Create a custom theme, which will contain the logo, or picture you want to show on the splash
2. Create a layout with just the theme on it.
3. Set the activity's content view to the previously created layout
4. Call the navigation with a delayed Intent

### Coin screen - List of coins
The list is a basic RecyclerView, each card has the logo; the name; the symbol; and the price (in USD) of the coin. And an additional checkbox.

![Coin List](https://i.imgur.com/P46MAk6.jpg)

### New coin screen - Add new crypto coin
There is an option to add a coin to the existing set. Which means an _add to the database_. Once you added it, you will see it on the Coins screen at the bottom of the list.

This is eventually a demo of the insertion logic of the Room persistence library, and add a use-case to test the Crashlytics.

![New coin](https://i.imgur.com/1Ylk4Ao.jpg)

## Populating the coin list

As I mentioned above, after the splash screen, the CoinsActivity has been started.

There is a CoinInteractor for the presenter, which will call the API for the coin data.

```
public class CoinInteractor {
    CoinApi coinApi;

    @Inject
    public CoinInteractor(CoinApi coinApi) {
        this.coinApi = coinApi;
        CoinCheckerApplication.injector.inject(this);
    }

    public void getCoins() {

        GetCoinsEvent event = new GetCoinsEvent();
        try {
            Call<CoinData> coinsResultCall = coinApi.getCoins(NetworkConfig.API_TOKEN,1,100,"USD");

            Response<CoinData> response = coinsResultCall.execute();
            if (response.code() != 200) {
                throw new Exception("Result code is not 200");
            }
            event.setCode(response.code());
            event.setCoins(response.body().getData());
            EventBus.getDefault().post(event);
        } catch (Exception e) {
            event.setThrowable(e);
            EventBus.getDefault().post(event);
        }
    }
}
```

It has a special event on the return, which has 3 attributes, one for the network code, one for the list of the coins, and a throwable object in case of some error.

The coinApi refers to this url: https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=100&convert=USD, with an API-key in the header as a value of the X-CMC_PRO_API_KEY.

Once the CoinInteractor does its job, this method delegates the results to the view - it is the responsibility of the presenter:

```
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(final GetCoinsEvent event) {
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
            if (screen != null) {
                screen.showNetworkError(event.getThrowable().getMessage());
            }
        } else {
            if (screen != null) {
                screen.showCoins(event.getCoins());
            }
        }
    }
```

## Persistence - with Room

How I handle the api calls, which can happen either with a navigation from main screen/new coin screen, or by a swipe refresh.

1. Get data from CoinMarketCap endpoint.
2. Save it to the DB. **Insert** if not already exists, and **update** is already there.
3. Modify the card of the item, by ticking/unticking the checkbox belongs to each item.
4. On the next fetch from the endpoint, we _need to consider the checkbox values_ (which I named as "favorite" field).

In the previous chapter, I show the navigation from the presenter
`screen.showCoins(event.getCoins());`
this line call the **CoinsFragment.showCoins**.

Refers to the **second and the third** point of the previous coin handle, this code snippet inserted:

```
        if (!coins.isEmpty()) {
            for (Coin coin : coins) {
                coin.setPrice(coin.getQuote().getUSD().getPrice());
                coin.setMarketCap(coin.getQuote().getUSD().getMarketCap());
                final Coin insertableCoin = coin;
                final LiveData<Boolean> inDbCoinFav = coinRepository.getCoinFav(coin.getId());
                inDbCoinFav.observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean aBoolean) {
                        if(aBoolean == null){
                            coinRepository.insertCoin(insertableCoin);
                        } else if(aBoolean){
                            Coin trueCoin = insertableCoin;
                            trueCoin.setFavorite(true);
                            coinRepository.updateCoin(trueCoin);
                        } else {
                            coinRepository.updateCoin(insertableCoin);
                        }

                        coinsList.add(insertableCoin);
                        inDbCoinFav.removeObserver(this);
                    }
                });
            }
        }
```

The interesting part of this is the Observer pattern.

This reactive technique allows us to handle the data fetch asynchronously from the DB, and when the data arrives, then we can check the results.

More about the favorite checkbox. The _CoinsAdapter_ handles the checkbox changing. Which also uses the Room db, by update a record in it.

```
        holder.cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                Coin toUpdate = coin;
                toUpdate.setFavorite(isChecked);
                CoinRepository coinRepository = new CoinRepository(context);
                if(coinRepository.getCoin(toUpdate.getId()) != null){
                    coinRepository.updateCoin(toUpdate);
                    Crashlytics.setString("EventAction","Favorite checkbox changed");
                } else {
                    //tick changed on a coin which has no id
                }
            }
        });
```

### CoinDao
This is the interface of the database I use.

```
@Dao
public interface CoinDao {

    @Insert
    Long insertCoin(Coin coin);

    @Query("SELECT max(id) FROM Coin")
    LiveData<Integer> getMaxId();


    @Query("SELECT * FROM Coin ORDER BY id asc")
    LiveData<List<Coin>> fetchAllCoins();


    @Query("SELECT * FROM Coin WHERE id =:coinId")
    LiveData<Coin> getCoin(int coinId);


    @Update
    void updateCoin(Coin coin);


    @Delete
    void deleteTask(Coin coin);

    @Query("SELECT favorite FROM Coin WHERE id =:coinId")
    LiveData<Boolean> getCoinFav(int coinId);
}
```

### CoinRepository

This is the class, which handles an instance of the Database.

One **interesting** part of the constructor of the CoinRepository, is the _fallbackToDestructiveMigration_ method. 
Which allows you to modify the structure of the database (altering the tables) and then deploy the application with the new structure of the database.

```
public class CoinRepository {

    private String DB_NAME = "db_coin";

    private CoinDatabase coinDatabase;

    public CoinRepository(Context context) {
        coinDatabase = Room.databaseBuilder(context, CoinDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
    }

    public void insertCoin(Integer id, String name, String symbol, Double price, Double marketcap) {
        Coin coin = new Coin(id, name, symbol, price, marketcap);
        insertCoin(coin);
    }

    @SuppressLint("StaticFieldLeak")
    public void insertCoin(final Coin coin) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                coinDatabase.daoAccess().insertCoin(coin);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateCoin(final Coin coin) {
        coin.setDateAdded(Calendar.getInstance().getTime().toString());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                coinDatabase.daoAccess().updateCoin(coin);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void deleteCoin(final Coin coin) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                coinDatabase.daoAccess().deleteTask(coin);
                return null;
            }
        }.execute();
    }

    public LiveData<Coin> getCoin(int id) {
        return coinDatabase.daoAccess().getCoin(id);
    }

    public LiveData<Boolean> getCoinFav(int id){return coinDatabase.daoAccess().getCoinFav(id);}

    public LiveData<List<Coin>> getCoins() {
        return coinDatabase.daoAccess().fetchAllCoins();
    }

    public LiveData<Integer> getMaxId() {
        return coinDatabase.daoAccess().getMaxId();
    }
}
```

### Model

I use only one table in this application, but the structure of returning JSON of the API call indicates to use more tables with a lot of possible variations to join them.
So the **Coin** class is my only class with the @Entity annotation. 

## Testing - Robolectric

### MainScreenTest
Testing the open of the MainScreen, and whether the showCoins method has been called.

#### @Before - setup section
1. Instantiates a Dagger test component
2. Use the injecting
3. Mock the screen
4. Call the attach directly

#### @After - tearDown
Detach directly

#### @Test
Used the _verify_ method to check the method call.

### CoinsTest
Testing the list. Mock API.

#### MockCoinApi - Override the getCoins method.
```
public class MockCoinApi implements CoinApi {
    @Override
    public Call<CoinData> getCoins(String authorisation, int start, int limit, String convert) {
        final CoinData coins = new CoinData();
        List<CoinData> coinDataList = new ArrayList<CoinData>();
        List<Coin> coinList = new ArrayList<>();
        Coin item = new Coin(1,"test coin","TC",100.0,10000.23);
        coinList.add(item);
        coins.setData(coinList);
        coinDataList.add(coins);

        Call<CoinData> call = new Call<CoinData>() {
            @Override
            public Response<CoinData> execute() throws IOException {
                return Response.success(coins);
            }

            @Override
            public void enqueue(Callback<CoinData> callback) {

            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<CoinData> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };

        return call;
    }
}
```

#### @Test

1. Force a refresh directly - this leads to a new fetch from the rest client.
2. ArgumentCaptor<List> - it is a tool to mocking a list.
3. If the captured list has more then 0 records, then the mocked api has been called

```
    public void testCoins() {
        coinsPresenter.refreshCoins();

        ArgumentCaptor<List> coinsCaptor = ArgumentCaptor.forClass(List.class);
        verify(coinsScreen).showCoins(coinsCaptor.capture());
        assertTrue(coinsCaptor.getValue().size() > 0);
    }
```

### NewCoinTest

Room persistency test.

#### Init - attributes, create -and close database
To test the DB, create one.

```
    private CoinDao coinDao;
    private CoinDatabase coinDatabase;
    @Inject
    NewCoinPresenter newCoinPresenter;
    private NewCoinScreen newCoinScreen;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        coinDatabase = Room.databaseBuilder(context, CoinDatabase.class, "db_coin").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        coinDao = coinDatabase.daoAccess();
        DaggerTestComponent injector = setTestInjector();
        injector.inject(this);
        newCoinScreen = mock(NewCoinScreen.class);
        newCoinPresenter.attachScreen(newCoinScreen);
    }

    @After
    public void closeDb() throws IOException {
        coinDatabase.close();
    }
```

#### Save logic
Very basic test. Add a function call through presenter.
Verify that the right method called in the screen with the exact same parameters.

```
        newCoinPresenter.addNewCoin("triggered", "T","100.1","212121.21");
        verify(newCoinScreen).newCoin("triggered", "T",100.1,212121.21);
```

#### Insertion
**Interesting** part: Use LiveData and Observer in a test.

```
@Test
    public void testInsertionLogic() throws Exception {
        final Coin coin = new Coin(1,"test coin","TC",100.0,10000.23);
        coinDao.insertCoin(coin);
        LiveData<Coin> insertedCoin = coinDao.getCoin(1);
        ActivityController controller = Robolectric.buildActivity(NewCoinActivity.class).create().start();
        AppCompatActivity activity = (AppCompatActivity) controller.get();

        LifecycleOwner lifecycle = mock(LifecycleOwner.class);

        insertedCoin.observe(activity, new Observer<Coin>() {
            @Override
            public void onChanged(@Nullable Coin newCoin) {
                if(newCoin != null){
                    assertEquals("matching",newCoin,coin);
                }
            }
        });
    }
```

## Analitycs

To test the _Crashlytics_ I left one exception to show on dashboard. 

Go to the Add coin screen, and push the add button with leave all the fields empty. This exception has been shown in the Dashboard, with a very detailed stack trace.
