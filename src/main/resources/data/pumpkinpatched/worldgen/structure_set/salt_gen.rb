# salt_gen.rb
def generate_salt(name)
  salt = name.chars.map(&:ord).sum * 1000
  puts "#{name}: #{salt}"
end
names = ["big_pumpkin"]
names.each do |name|
  generate_salt(name)
end