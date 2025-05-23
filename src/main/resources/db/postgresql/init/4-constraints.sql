alter table tool.books add constraint fk_f65lsitwn7k54dkh1jxyoc825 foreign key (category_id) references tool.book_categories (id);
alter table tool.books add constraint fk_sinsbyrnio38x1dx04n7nep3r foreign key (press_id) references tool.presses (id);
alter table tool.his_books add constraint fk_j1do9c4sgorm47m19swxojxsx foreign key (category_id) references tool.book_categories (id);
alter table tool.his_books add constraint fk_oj2aanst0dsnuj0u0k5t3vnm5 foreign key (press_id) references tool.presses (id);
