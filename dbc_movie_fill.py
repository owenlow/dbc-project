""" 
dbc_movie_fill.py
DB movie data insertion
Author: Owen Royall-Kahin
"""

import psycopg2
import sys

SCORE_THRESHOLD = 7.0
RATINGS_FILENAME = "ratings-list.tsv"
GENRES_FILENAME = "genres-list.tsv"
MOVIES_FILENAME = "movies.tsv"

DATABASE = 'p48501a'
USERNAME = 'p48501a'
PASSWORD = 'uixohphieshiechu'
HOST = 'reddwarf.cs.rit.edu'

def main():
	# ratings-list.tsv: ( score \t title \t year ) 
	# genre-list.tsv: ( ??? )
	con = None
	try: 
		con = psycopg2.connect(database=DATABASE, user=USERNAME, password=PASSWORD, host=HOST)
		
		cur = con.cursor()
		
		insert_all_movies(cur)
		
		print "committing"
		
		con.commit()
		
		
	except psycopg2.DatabaseError, e:    
		if con:
			con.rollback()

		print 'Error %s' % e    
		sys.exit(1)
	
	finally:	
		if con:
			con.close()
	
	print "exiting"
	return
	

def insert_all_movies(cur):
	print "opening file"
	movie_file = open(MOVIES_FILENAME, "r")
	
	movie_dict = {}
	# id title countrycode year rating score [genres]
	print "parsing file"
	count = 0
	for line in movie_file:
		if count != 0:
			movie = line.split('\t')
			if movie[4] <= SCORE_THRESHOLD:
				continue
			movie_dict[movie[0]] = movie;
		count += 1
		
	print "creating insert statements"
	for movie in movie_dict.values():
		cur.execute("insert into movie (movie_id, title, year, rating, score) \
			values (" + movie[0] + ", '" + escape(movie[1]) + "', " + movie[3] + ", '" + movie[4] + "', " + movie[5] + ")")
		for g in movie[6:]:
			cur.execute("insert into genre( movie_id, genre ) values \
			(" + movie[0] + ", '" + g + "')")
			
	print "about to commit " + count + " rows"
	
	
def escape(seq):
	seq = seq.replace("'", "''")
	print seq
	return seq
	
if __name__ == "__main__":
	main()
