document.addEventListener("DOMContentLoaded", function () {
    // Example: Update random wallpaper and movie title
    const randomWallpaper = {
        title: "The Shawshank Redemption",
        img: "/images/shawshank.jpg"
    };

    document.getElementById("random-movie-img").src = randomWallpaper.img;
    document.getElementById("movie-title").textContent = randomWallpaper.title;

    // TODO: Add API calls for "Most Searched" and "Suggestions"
});