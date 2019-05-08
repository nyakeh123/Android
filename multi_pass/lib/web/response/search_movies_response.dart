import 'package:intl/intl.dart';
import 'package:multi_pass/web/secrets.dart';

class SearchMoviesResponse {
  int page;
  int totalResults;
  List<SearchMoviesResult> results;

  SearchMoviesResponse(this.page, this.totalResults, this.results);

  factory SearchMoviesResponse.fromJson(Map<String, dynamic> parsedJson) {
    var resultsJson = parsedJson['results'];
    var resultsList = new List<SearchMoviesResult>.from(resultsJson.map((value) => SearchMoviesResult.fromJson(value)));
    return new SearchMoviesResponse(parsedJson['page'], parsedJson['total_results'], resultsList);
  }
}

class SearchMoviesResult {
  int id;
  String title;
  String overview;
  String posterPath;
  DateTime releaseDate;
  List<int> genreIds;

  SearchMoviesResult(this.id, this.title, this.overview, this.posterPath, this.releaseDate, this.genreIds);

  factory SearchMoviesResult.fromJson(Map<String, dynamic> parsedJson) {
    var genreIdsJson = parsedJson['genre_ids'];
    var genreIdsList = new List<int>.from(genreIdsJson);
    DateFormat format = new DateFormat("yyyy-MM-dd");
    var releaseDate = new DateTime(1972);
    RegExp regex = new RegExp("(\d{4}-\d{2}-\d{2})");
    if (regex.hasMatch(parsedJson['release_date'])) {
      releaseDate = format.parse(parsedJson['release_date']);
    }
    var posterPath = Secrets().getTheMovieDatabaseImageBaseUrl() + parsedJson['poster_path'];
    return new SearchMoviesResult(parsedJson['id'], parsedJson['title'], parsedJson['overview'], posterPath, releaseDate, genreIdsList);
  }
}
