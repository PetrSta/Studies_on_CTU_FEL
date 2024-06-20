from rosemary import Item, update


def test_diamond():
    item = Item(name='Diamond', days_left=5, quality=100)
    update(item)
    return item.name == 'Diamond' and item.days_left == 5 and item.quality == 100


def test_normal_item_decreasing_quality_and_days():
    item = Item(name='Bread', days_left=4, quality=10)
    update(item)
    return item.name == 'Bread' and item.days_left == 3 and item.quality == 9


def test_expired_exactly_item_decreasing_quality_and_days():
    item = Item(name='Ham', days_left=0, quality=9)
    update(item)
    return item.name == 'Ham' and item.days_left == -1 and item.quality == 7


def test_expired_item_decreasing_quality_and_days():
    item = Item(name='Ham', days_left=-6, quality=9)
    update(item)
    return item.name == 'Ham' and item.days_left == -7 and item.quality == 7


def test_normal_item_minimal_quality():
    item = Item(name='Bread', days_left=5, quality=0)
    update(item)
    return item.name == 'Bread' and item.days_left == 4 and item.quality == 0


def test_expired_item_minimal_quality():
    item = Item(name='Ham', days_left=-2, quality=1)
    update(item)
    return item.name == 'Ham' and item.days_left == -3 and item.quality == 0


def test_expired_exactly_item_minimal_quality():
    item = Item(name='Ham', days_left=0, quality=1)
    update(item)
    return item.name == 'Ham' and item.days_left == -1 and item.quality == 0


def test_expired_item_minimal_quality_already_0():
    item = Item(name='Ham', days_left=0, quality=0)
    update(item)
    return item.name == 'Ham' and item.days_left == -1 and item.quality == 0


def test_expired_item_minimal_quality_already_0_negative_days():
    item = Item(name='Ham', days_left=-5, quality=0)
    update(item)
    return item.name == 'Ham' and item.days_left == -6 and item.quality == 0


def test_aged_brie_basic():
    item = Item(name='Aged Brie', days_left=5, quality=10)
    update(item)
    return item.name == 'Aged Brie' and item.days_left == 4 and item.quality == 11


def test_aged_brie_expired():
    item = Item(name='Aged Brie', days_left=-5, quality=20)
    update(item)
    return item.name == 'Aged Brie' and item.days_left == -6 and item.quality == 21


def test_aged_brie_expired_exactly():
    item = Item(name='Aged Brie', days_left=0, quality=20)
    update(item)
    return item.name == 'Aged Brie' and item.days_left == -1 and item.quality == 21


def test_aged_brie_maximum():
    item = Item(name='Aged Brie', days_left=5, quality=50)
    update(item)
    return item.name == 'Aged Brie' and item.days_left == 4 and item.quality == 50


def test_aged_brie_expired_maximum():
    item = Item(name='Aged Brie', days_left=-13, quality=50)
    update(item)
    return item.name == 'Aged Brie' and item.days_left == -14 and item.quality == 50


def test_aged_brie_expired_exactly_maximum():
    item = Item(name='Aged Brie', days_left=0, quality=50)
    update(item)
    return item.name == 'Aged Brie' and item.days_left == -1 and item.quality == 50


def test_tickets_more_than_10_days():
    item = Item(name='Tickets', days_left=11, quality=10)
    update(item)
    return item.name == 'Tickets' and item.days_left == 10 and item.quality == 11


def test_tickets_10_days():
    item = Item(name='Tickets', days_left=10, quality=10)
    update(item)
    return item.name == 'Tickets' and item.days_left == 9 and item.quality == 12


def test_tickets_between_10_and_5_days():
    item = Item(name='Tickets', days_left=7, quality=10)
    update(item)
    return item.name == 'Tickets' and item.days_left == 6 and item.quality == 12


def test_tickets_6_days():
    item = Item(name='Tickets', days_left=6, quality=10)
    update(item)
    return item.name == 'Tickets' and item.days_left == 5 and item.quality == 12


def test_tickets_5_days():
    item = Item(name='Tickets', days_left=5, quality=10)
    update(item)
    return item.name == 'Tickets' and item.days_left == 4 and item.quality == 13


def test_tickets_less_than_5_days():
    item = Item(name='Tickets', days_left=3, quality=10)
    update(item)
    return item.name == 'Tickets' and item.days_left == 2 and item.quality == 13


def test_tickets_1_day():
    item = Item(name='Tickets', days_left=1, quality=10)
    update(item)
    return item.name == 'Tickets' and item.days_left == 0 and item.quality == 13


def test_tickets_0_days():
    item = Item(name='Tickets', days_left=0, quality=10)
    update(item)
    return item.name == 'Tickets' and item.days_left == -1 and item.quality == 0


def test_tickets_negative_days():
    item = Item(name='Tickets', days_left=-5, quality=0)
    update(item)
    return item.name == 'Tickets' and item.days_left == -6 and item.quality == 0


def test_tickets_more_than_10_days_maximum():
    item = Item(name='Tickets', days_left=11, quality=50)
    update(item)
    return item.name == 'Tickets' and item.days_left == 10 and item.quality == 50


def test_tickets_10_days_maximum():
    item = Item(name='Tickets', days_left=10, quality=50)
    update(item)
    return item.name == 'Tickets' and item.days_left == 9 and item.quality == 50


def test_tickets_between_10_and_5_days_maximum():
    item = Item(name='Tickets', days_left=7, quality=50)
    update(item)
    return item.name == 'Tickets' and item.days_left == 6 and item.quality == 50


def test_tickets_6_days_maximum():
    item = Item(name='Tickets', days_left=6, quality=50)
    update(item)
    return item.name == 'Tickets' and item.days_left == 5 and item.quality == 50


def test_tickets_5_days_maximum():
    item = Item(name='Tickets', days_left=5, quality=50)
    update(item)
    return item.name == 'Tickets' and item.days_left == 4 and item.quality == 50


def test_tickets_less_than_5_days_maximum():
    item = Item(name='Tickets', days_left=3, quality=50)
    update(item)
    return item.name == 'Tickets' and item.days_left == 2 and item.quality == 50


def test_tickets_1_day_maximum():
    item = Item(name='Tickets', days_left=1, quality=50)
    update(item)
    return item.name == 'Tickets' and item.days_left == 0 and item.quality == 50
