package com.example.abc.newsaggregator.activities;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.abc.newsaggregator.adapters.ArticleAdapter;
import com.example.abc.newsaggregator.adapters.SourceItemAdapter;
import com.example.abc.newsaggregator.models.ArticleModel;
import com.example.abc.newsaggregator.models.CountryModel;
import com.example.abc.newsaggregator.models.LanguageModel;
import com.example.abc.newsaggregator.models.SourceModel;
import com.example.abc.newsaggregator.controllers.ArticlesLoaderRunnable;
import com.example.abc.newsaggregator.controllers.ColorsLoader;
import com.example.abc.newsaggregator.controllers.ConnectionUtils;
import com.example.abc.newsaggregator.controllers.CountriesLoader;
import com.example.abc.newsaggregator.controllers.LanguagesLoader;
import com.example.abc.newsaggregator.controllers.SourcesDataLoaderRunnable;
import com.example.abc.newsaggregator.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private Menu menu;
    private DrawerLayout newsDrawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private ArticleAdapter articleAdapter;
    private ViewPager2 viewPager;

    private ArrayList<SourceModel> sources = new ArrayList<>();
    private List<SourceModel> filteredSources = new ArrayList<>();

    private ArrayList<LanguageModel> languages = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<CountryModel> countries = new ArrayList<>();

    private Map<String, String> allCountries = new HashMap<>();
    private Map<String, String> allLanguages = new HashMap<>();

    private final ArrayList<ArticleModel> articles = new ArrayList<>();
    private ArrayList<String> colors = new ArrayList<>();
    public Map<String, String> colorCategories = new HashMap<>();

    private String selectedLanguage;
    private String selectedCategory;
    private String selectedCountry;
    private SourceModel selectedSource;

    private static final int CATEGORIES_ID = 0;

    //
    //
    //this method will initialize all the data on crate
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsDrawerLayout = findViewById(R.id.drawer_layout);
        drawerList = findViewById(R.id.drawer);

        drawerList.setOnItemClickListener((parent, view, position, id) -> topicSelected(position));
        drawerToggle = new ActionBarDrawerToggle(this, newsDrawerLayout, R.string.open_drawer,
                R.string.close_drawer);

        articleAdapter = new ArticleAdapter(this, articles);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(articleAdapter);

        loadData();
    }

    //
    //
    //
    private void loadData() {
        new Thread(new LanguagesLoader(this)).start();
        new Thread(new CountriesLoader(this)).start();
        new Thread(new ColorsLoader(this)).start();

        if (ConnectionUtils.hasNetworkConnection(this)) {
            new Thread(new SourcesDataLoaderRunnable(this)).start();
        } else {
            Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }
    }

    //
    //
    //this method will be called after selecting an item from options
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if (item.hasSubMenu()) {
            return true;
        }
        selectedCategory = item.getItemId() > 0 ? categories.get(item.getItemId() - 1) : null;

        filterSources();

        return super.onOptionsItemSelected(item);
    }
    //
    //
    //

    //this method will create a sub menu called topics which will include all the topics
    private void createSubMenu(int groupId, String groupName, List<String> values,
                               boolean colorable) {
        SubMenu sb = menu.addSubMenu(groupName);
        sb.add(groupId, 0, 0, getString(R.string.all));
        for (int i = 1; i <= values.size(); i++) {
            MenuItem item = sb.add(groupId, i, i, values.get(i - 1));
            if (colorable) {
                SpannableString s = new SpannableString(values.get(i - 1));
                s.setSpan(new ForegroundColorSpan(
                                Color.parseColor(colorCategories.get(values.get(i - 1)))), 0,
                        s.length(),
                        0);
                item.setTitle(s);
            }
        }
    }

    //
    //
    // we need this to set up the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    //
    //
    //this method will be called upon updating sources
    public void updatingSourcesSuccess(ArrayList<SourceModel> sources, Set<String> languages,
                                       Set<String> categories, Set<String> countries) {

        this.sources = sources;
        this.categories =
                categories.stream().sorted().collect(Collectors.toCollection(ArrayList::new));

        mapCategoriesAndColors();

        createSubMenu(CATEGORIES_ID, getString(R.string.topics), this.categories, true);

        filterSources();
    }

    //
    //
    //this method will be called for syncing the toggle state
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    //
    //
    //this method will be called after configuration change
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggle
        drawerToggle.onConfigurationChanged(newConfig);
    }


    //
    //
    //this method will fetch the data which is required to display on article page
    @SuppressLint("NotifyDataSetChanged")
    public void fetchingArticlesSuccess(List<ArticleModel> articles) {

        this.articles.clear();
        this.articles.addAll(articles);

        articleAdapter.notifyDataSetChanged();

        if (this.articles.isEmpty()) {
            Toast.makeText(this, String.format(Locale.getDefault(), getString(R.string.no_articles),
                    selectedSource.getName()), Toast.LENGTH_SHORT).show();
            return;
        }

        viewPager.setBackground(null);
        viewPager.setCurrentItem(0);

        setTitle(selectedSource.getName());
    }

    //
    //
    //this method will retrieve countries from the news API
    public void receiveFullCountries(Map<String, String> countries) {
        allCountries = countries;

        if (allCountries == null) {
            Toast.makeText(this, getString(R.string.error_countries), Toast.LENGTH_SHORT).show();
        }
    }

    //
    //
    //this method will retrieve languages from the news API
    public void receiveFullLanguages(Map<String, String> languages) {

        allLanguages = languages;

        if (allLanguages == null) {
            Toast.makeText(this, getString(R.string.error_languages), Toast.LENGTH_SHORT).show();
        }
    }

    //
    //
    //setting colors
    public void receiveColors(ArrayList<String> colors) {

        this.colors = colors;
    }



    public void updatingSourcesFailed() {
        Toast.makeText(this, getString(R.string.error_sources), Toast.LENGTH_SHORT).show();
    }

    public void fetchingArticlesFailed() {
        Toast.makeText(this, getString(R.string.error_articles), Toast.LENGTH_SHORT).show();
    }

    //
    //
    //this method will save the instance of the app while rotating
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(getString(R.string.selected_category), selectedCategory);
        outState.putString(getString(R.string.selected_language), selectedLanguage);
        outState.putString(getString(R.string.selected_country), selectedCountry);

        outState.putStringArrayList(getString(R.string.categories), categories);
        outState.putParcelableArrayList(getString(R.string.languages), languages);
        outState.putParcelableArrayList(getString(R.string.countries), countries);
        outState.putParcelableArrayList(getString(R.string.sources), sources);

        outState.putParcelable(getString(R.string.selected_source), selectedSource);
        outState.putParcelableArrayList(getString(R.string.articles), articles);
        outState.putInt(getString(R.string.current_article), viewPager.getCurrentItem());

        outState.putStringArrayList(getString(R.string.colors), colors);

        super.onSaveInstanceState(outState);
    }

    //
    //
    //this method will restore the instance of the app while rotating
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        selectedCategory = savedInstanceState.getString(getString(R.string.selected_category));
        selectedLanguage = savedInstanceState.getString(getString(R.string.selected_language));
        selectedCountry = savedInstanceState.getString(getString(R.string.selected_country));

        categories = savedInstanceState.getStringArrayList(getString(R.string.categories));
        languages = savedInstanceState.getParcelableArrayList(getString(R.string.languages));
        countries = savedInstanceState.getParcelableArrayList(getString(R.string.countries));
        sources = savedInstanceState.getParcelableArrayList(getString(R.string.sources));

        colors = savedInstanceState.getStringArrayList(getString(R.string.colors));
        mapCategoriesAndColors();

        articles.addAll(savedInstanceState.getParcelableArrayList(getString(R.string.articles)));

        if (!articles.isEmpty()) {
            articleAdapter.notifyDataSetChanged();
            viewPager.setBackground(null);
            viewPager
                    .setCurrentItem(savedInstanceState.getInt(getString(R.string.current_article)));
        }

        selectedSource = savedInstanceState.getParcelable(getString(R.string.selected_source));

        filterSources();

        if (selectedSource != null) {
            setTitle(selectedSource.getName());
        }

    }

    //
    //
    //
    private void topicSelected(int position) {
        selectedSource = filteredSources.get(position);
        if (this.selectedSource != null) {
            new Thread(new ArticlesLoaderRunnable(selectedSource.getId(), this)).start();
            newsDrawerLayout.closeDrawer(drawerList);
        }
    }

    //
    //
    //this method will show a message if there is no valid source present
    private void showNoSourcesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("No sources available");
        builder.setMessage(
                "No sources exist matching the specified Topic, Language and/or Country");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //
    //
    //this method will filter the results according to the language, type and country
    private void filterSources() {
        filteredSources = sources.stream()
                .filter(s -> selectedLanguage == null || s.getLanguage().equals(selectedLanguage))
                .filter(s -> selectedCategory == null || s.getCategory().equals(selectedCategory))
                .filter(s -> selectedCountry == null || s.getCountry().equals(selectedCountry))
                .collect(Collectors.toList());

        if (selectedSource == null) {
            // Default title is only shown if no source has been selected
            setTitle(String.format(Locale.getDefault(), "%s (%d)", getString(R.string.app_name),
                    filteredSources.size()));
        }

        if (filteredSources.size() == 0) {
            showNoSourcesDialog();
        }

        drawerList.setAdapter(new SourceItemAdapter(this, R.layout.drawer_list_item,
                filteredSources.toArray(new SourceModel[0])));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    //
    //
    //this method will map the article topics according to the category
    private void mapCategoriesAndColors() {
        for (int i = 0; i < this.categories.size(); i++) {
            String color;
            if (colors != null && (colors.size()) > i) {
                color = colors.get(i);
            } else {
                color = "#000000";  // Default color is black
            }

            colorCategories.put(this.categories.get(i), color);
        }
    }
}
